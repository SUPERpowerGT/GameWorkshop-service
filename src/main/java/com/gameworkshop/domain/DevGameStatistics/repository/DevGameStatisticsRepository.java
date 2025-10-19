package com.gameworkshop.domain.DevGameStatistics.repository;

import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import java.util.Optional;

public interface DevGameStatisticsRepository {
    Optional<DevGameStatistics> findByGameId(String gameId);
    void insert(DevGameStatistics stats);
    void updateCounts(String gameId, int viewIncrement, int downloadIncrement);
}
