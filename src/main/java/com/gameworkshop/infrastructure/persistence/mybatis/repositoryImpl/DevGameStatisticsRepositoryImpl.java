package com.gameworkshop.infrastructure.persistence.mybatis.repositoryImpl;

import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import com.gameworkshop.domain.DevGameStatistics.repository.DevGameStatisticsRepository;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameStatisticsMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class DevGameStatisticsRepositoryImpl implements DevGameStatisticsRepository {

    private final DevGameStatisticsMapper devGameStatisticsMapper;

    @Override
    public Optional<DevGameStatistics> findByGameId(String gameId) {
        return devGameStatisticsMapper.findByGameId(gameId);
    }

    @Override
    public void insert(DevGameStatistics stats) {
        devGameStatisticsMapper.insert(stats);
    }

    @Override
    public void updateCounts(String gameId, int viewIncrement, int downloadIncrement) {
        devGameStatisticsMapper.updateCounts(gameId, viewIncrement, downloadIncrement);
    }
}