package com.gameworkshop.application.service;

import com.gameworkshop.infrastructure.cache.DevGameStatisticsCache;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 应用层服务：负责统计相关的业务逻辑
 */
@Service
@RequiredArgsConstructor
public class DevGameStatisticsApplicationService {

    private final DevGameStatisticsCache statisticsCache;

    /**
     * 用户浏览游戏详情 → 增加浏览量
     */
    @Transactional
    public void recordGameView(String gameId) {
        statisticsCache.incrementView(gameId);
    }

    /**
     * 用户下载游戏 → 增加下载量
     */
    @Transactional
    public void recordGameDownload(String gameId) {
        statisticsCache.incrementDownload(gameId);
    }

    /**
     * 获取当前缓存统计（用于调试或展示）
     */
    public long getViewCount(String gameId) {
        return statisticsCache.getViewCount(gameId);
    }

    public long getDownloadCount(String gameId) {
        return statisticsCache.getDownloadCount(gameId);
    }
}
