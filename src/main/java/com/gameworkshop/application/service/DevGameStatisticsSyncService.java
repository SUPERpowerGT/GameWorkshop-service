package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import com.gameworkshop.domain.DevGameStatistics.repository.DevGameStatisticsRepository;
import com.gameworkshop.infrastructure.cache.DevGameStatisticsCache;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class DevGameStatisticsSyncService {

    private final DevGameStatisticsCache cache;
    private final DevGameStatisticsRepository repository;

    /**
     * 每5分钟同步一次 Redis 数据到 PostgreSQL
     */
    @Scheduled(cron = "0 */5 * * * *")
    public void syncStatisticsFromRedis() {
        log.info("🚀 [SYNC] Starting Redis -> PostgreSQL sync...");

        Set<String> viewKeys = cache.getKeysByPrefix("devgame:view:");
        if (viewKeys == null || viewKeys.isEmpty()) {
            log.info("✅ [SYNC] No data to sync.");
            return;
        }

        for (String key : viewKeys) {
            try {
                String gameId = key.replace("devgame:view:", "");
                Long viewCount = cache.getViewCount(gameId);
                Long downloadCount = cache.getDownloadCount(gameId);

                Optional<DevGameStatistics> optional = repository.findByGameId(gameId);
                DevGameStatistics existing = optional.orElse(null);

                if (existing == null) {
                    DevGameStatistics stats = new DevGameStatistics(
                            java.util.UUID.randomUUID().toString(),
                            gameId,
                            viewCount.intValue(),
                            downloadCount.intValue(),
                            0.0,
                            LocalDateTime.now()
                    );
                    repository.insert(stats);
                } else {
                    repository.updateCounts(gameId, viewCount.intValue(), downloadCount.intValue());
                }

            } catch (Exception e) {
                log.error("❌ [SYNC] Failed to sync for key: {}", key, e);
            }
        }

        log.info("✅ [SYNC] Redis data sync complete!");
    }
}
