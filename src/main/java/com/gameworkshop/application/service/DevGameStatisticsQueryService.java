package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.infrastructure.cache.DevGameStatisticsCache;
import com.gameworkshop.infrastructure.util.AssetUrlBuilder;
import com.gameworkshop.interfaces.dto.HotGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DevGameStatisticsQueryService {

    private final DevGameStatisticsCache cache;
    private final DevGameRepository devGameRepository;
    private final DevGameAssetRepository devGameAssetRepository;
    private final AssetUrlBuilder assetUrlBuilder;

    /**
     * 获取热门游戏（根据浏览量 + 下载量计算）
     */
    public List<HotGameResponse> getHotGames(int limit) {
        Set<String> viewKeys = cache.getKeysByPrefix("devgame:view:");
        if (viewKeys == null || viewKeys.isEmpty()) return Collections.emptyList();

        List<HotGameResponse> results = new ArrayList<>();

        for (String key : viewKeys) {
            String gameId = key.replace("devgame:view:", "");
            long viewCount = cache.getViewCount(gameId);
            long downloadCount = cache.getDownloadCount(gameId);

            // 简单热度算法，可自定义加权
            double score = viewCount * 0.7 + downloadCount * 1.3;

            devGameRepository.findById(gameId).ifPresent(game -> {
                String coverUrl = devGameAssetRepository
                        .findFirstByGameIdAndType(game.getId(), "image")
                        .map(asset -> assetUrlBuilder.buildDownloadUrl(asset.getId()))
                        .orElse(null);

                results.add(new HotGameResponse(
                        game.getId(),
                        game.getName(),
                        game.getDescription(),
                        coverUrl,
                        score
                ));
            });
        }

        // 排序取前 N
        return results.stream()
                .sorted(Comparator.comparingDouble(HotGameResponse::getScore).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
