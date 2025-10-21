package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import com.gameworkshop.domain.DevGameStatistics.repository.DevGameStatisticsRepository;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.DevDashboardDetailedResponse;
import com.gameworkshop.interfaces.dto.DevGameStatsDetailDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevGameStatisticsDashboardService {

    private final DevGameRepository devGameRepository;
    private final DevGameStatisticsRepository devGameStatisticsRepository;
    private final DeveloperProfileRepository developerProfileRepository;

    /**
     * ✅ 根据 developerId 获取仪表盘详情
     */
    public DevDashboardDetailedResponse getDashboardDetails(String developerId) {
        List<DevGame> games = devGameRepository.findByDeveloperProfileId(developerId);

        // ====== 构建每个游戏明细 ======
        List<DevGameStatsDetailDTO> details = new ArrayList<>();

        for (DevGame game : games) {
            Optional<DevGameStatistics> statsOpt = devGameStatisticsRepository.findByGameId(game.getId());

            if (statsOpt.isPresent()) {
                DevGameStatistics stats = statsOpt.get();
                details.add(new DevGameStatsDetailDTO(
                        game.getId(),
                        game.getName(),
                        stats.getViewCount(),
                        stats.getDownloadCount(),
                        stats.getRating(),
                        stats.getUpdatedAt()
                ));
            } else {
                // 没有统计记录的游戏
                details.add(new DevGameStatsDetailDTO(
                        game.getId(),
                        game.getName(),
                        0,
                        0,
                        0.0,
                        game.getUpdatedAt()
                ));
            }
        }

        // ====== 计算汇总数据 ======
        int totalViews = details.stream().mapToInt(DevGameStatsDetailDTO::getViewCount).sum();
        int totalDownloads = details.stream().mapToInt(DevGameStatsDetailDTO::getDownloadCount).sum();

        double avgRating = details.stream()
                .mapToDouble(DevGameStatsDetailDTO::getRating)
                .filter(r -> r > 0)
                .average()
                .orElse(0.0);

        // ====== 构建 Summary 对象 ======
        DevDashboardDetailedResponse.Summary summary =
                new DevDashboardDetailedResponse.Summary(
                        games.size(),
                        totalViews,
                        totalDownloads,
                        avgRating
                );

        // ====== 返回组合结果 ======
        return new DevDashboardDetailedResponse(developerId, summary, details);
    }

    /**
     * ✅ 根据 userId 获取仪表盘详情
     *    若用户还不是开发者，则返回空 Dashboard
     */
    public DevDashboardDetailedResponse getDashboardByUserId(String userId) {
        Optional<DeveloperProfile> devOpt = developerProfileRepository.findByUserId(userId);

        if (devOpt.isEmpty()) {
            // 用户还不是开发者 → 返回空 Dashboard
            DevDashboardDetailedResponse.Summary emptySummary =
                    new DevDashboardDetailedResponse.Summary(0, 0, 0, 0.0);

            return new DevDashboardDetailedResponse(
                    userId,                     // developerId（此时暂用 userId）
                    emptySummary,               // summary
                    Collections.emptyList()     // games
            );
        }

        DeveloperProfile dev = devOpt.get();
        return getDashboardDetails(dev.getId());
    }
}
