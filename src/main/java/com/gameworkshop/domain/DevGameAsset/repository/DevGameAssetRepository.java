package com.gameworkshop.domain.DevGameAsset.repository;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DevGameAssetRepository {
    Optional<DevGameAsset> findById(String id);

    List<DevGameAsset> findByGameId(String devGameId);

    List<DevGameAsset> findByGameIdAndType(String devGameId, String assetType);

    Optional<DevGameAsset> findFirstByGameIdAndType(String devGameId, String assetType);

    void insert(DevGameAsset asset);

    void update(DevGameAsset asset);

    void save(DevGameAsset asset);

    int batchSave(List<DevGameAsset> assets);

    boolean deleteById(String id);

    void deleteByGameId(String devGameId);

    long countByGameId(String devGameId);
}
