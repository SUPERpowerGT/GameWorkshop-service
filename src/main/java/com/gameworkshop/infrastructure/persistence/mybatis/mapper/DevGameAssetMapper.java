package com.gameworkshop.infrastructure.persistence.mybatis.mapper;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Optional;

@Mapper
public interface DevGameAssetMapper {

    Optional<DevGameAsset> findById(String id);


    List<DevGameAsset> findByDevGameId(String devGameId);


    List<DevGameAsset> findByDevGameIdAndAssetType(
            @Param("devGameId") String devGameId,
            @Param("assetType") String assetType);


    int insert(DevGameAsset devGameAsset);

    int batchInsert(@Param("assets") List<DevGameAsset> assets);

    int updateById(DevGameAsset devGameAsset);

    int deleteById(String id);

    int deleteByDevGameId(String devGameId);

    long countByDevGameId(String devGameId);
}
