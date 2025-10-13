package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
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

    /**
     * 获取指定开发者的所有游戏及其封面图信息
     */

    public List<DevGameSummaryResponse> listGamesWithCoverByDeveloper(String developerId) {
        // 查询该开发者的所有游戏
        List<DevGame> games = devGameRepository.findByDeveloperProfileId(developerId);

        // 将每个游戏映射成带封面图的响应对象
        return games.stream().map(game -> {
            // 获取封面图（类型为 "image"）
            String imageUrl = devGameAssetRepository
                    .findFirstByGameIdAndType(game.getId(), "image")
                    .map(asset -> assetBaseUrl + "/api/developer/devgameasset/download/" + asset.getId()) // 如果存在图片就返回下载链接
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
}
