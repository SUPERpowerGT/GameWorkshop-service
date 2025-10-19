package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGameStatistics.model.DevGameStatistics;
import org.apache.ibatis.annotations.Mapper;

import java.util.Optional;

@Mapper
public interface DevGameStatisticsMapper {

    Optional<DevGameStatistics> findByGameId(String gameId);

    void insert(DevGameStatistics stats);

    void updateCounts(String gameId, int viewIncrement, int downloadIncrement);
}
