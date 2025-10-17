package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.infrastructure.util.AssetUrlBuilder;
import com.gameworkshop.interfaces.dto.DevGameListResponse;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import com.gameworkshop.interfaces.dto.DevGameSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevGameQueryApplicationService {
    @Value("${app.asset-base-url}")
    private String assetBaseUrl;

    private final DevGameRepository devGameRepository;
    private final DevGameAssetRepository devGameAssetRepository;
    private final AssetUrlBuilder assetUrlBuilder;

    public List<DevGameSummaryResponse> listDevGamesWithCover(String developerId) {
        // 查询该开发者的所有游戏
        List<DevGame> games = devGameRepository.findByDeveloperProfileId(developerId);

        // 将每个游戏映射成带封面图的响应对象
        return games.stream().map(game -> {
            // 获取封面图（类型为 "image"）
            String imageUrl = devGameAssetRepository
                    .findFirstByGameIdAndType(game.getId(), "image")
                    .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId())) // 如果存在图片就返回下载链接
                    .orElse(null); // 没有图片则为 null

            return new DevGameSummaryResponse(
                    game.getId(),
                    game.getName(),
                    game.getDescription(),
                    game.getReleaseDate(),
                    game.getCreatedAt(),
                    imageUrl
            );
        }).collect(Collectors.toList());
    }

    public DevGameResponse queryDevGameDetails(String gameId) {
        DevGame game = devGameRepository.findById(gameId)
                .orElseThrow(() -> new IllegalArgumentException("Game not found: " + gameId));

        String imageUrl = devGameAssetRepository
                .findFirstByGameIdAndType(gameId, "image")
                .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId()))
                .orElse(null);

        String videoUrl = devGameAssetRepository
                .findFirstByGameIdAndType(gameId, "video")
                .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId()))
                .orElse(null);

        String zipUrl = devGameAssetRepository
                .findFirstByGameIdAndType(gameId, "zip")
                .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId()))
                .orElse(null);


        return new DevGameResponse(
                game.getId(),
                game.getName(),
                game.getDescription(),
                imageUrl,
                videoUrl,
                zipUrl
        );
    }

    public DevGameListResponse listAllGames(int page, int pageSize) {
        int offset = Math.max(page - 1, 0) * pageSize;

        // 1️⃣ 查询分页数据与总数
        List<DevGame> games = devGameRepository.findAllPaged(offset, pageSize);
        long totalCount = devGameRepository.countAll();
        int totalPages = (int) Math.ceil((double) totalCount / pageSize);

        // 2️⃣ 封装每个游戏的封面图URL
        List<DevGameSummaryResponse> summaries = games.stream().map(game -> {
            String imageUrl = devGameAssetRepository
                    .findFirstByGameIdAndType(game.getId(), "image")
                    .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId()))
                    .orElse(null);

            return new DevGameSummaryResponse(
                    game.getId(),
                    game.getName(),
                    game.getDescription(),
                    game.getReleaseDate(),
                    game.getCreatedAt(),
                    imageUrl
            );
        }).collect(Collectors.toList());

        // 3️⃣ 构建响应体
        return new DevGameListResponse(
                summaries,
                page,
                pageSize,
                totalCount,
                totalPages
        );
    }

}
