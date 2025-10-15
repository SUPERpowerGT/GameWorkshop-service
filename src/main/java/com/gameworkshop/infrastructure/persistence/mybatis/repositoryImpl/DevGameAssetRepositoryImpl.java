package com.gameworkshop.infrastructure.persistence.mybatis.repositoryImpl;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.infrastructure.persistence.mybatis.mapper.DevGameAssetMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
//@NoArgsConstructor空构造函数 @AllArgsConstructor所有字段的构造函数 @RequiredArgsConstructor必需参数构造函数（final/@NonNull）
@RequiredArgsConstructor
public class DevGameAssetRepositoryImpl implements DevGameAssetRepository {
    private final DevGameAssetMapper devGameAssetMapper;

    @Override
    public Optional<DevGameAsset> findById(String id) {
        return devGameAssetMapper.findById(id);
    }

    @Override
    public List<DevGameAsset> findByGameId(String devGameId) {
        return devGameAssetMapper.findByDevGameId(devGameId);
    }

    @Override
    public List<DevGameAsset> findByGameIdAndType(String devGameId, String assetType) {
        return devGameAssetMapper.findByDevGameIdAndAssetType(devGameId, assetType);
    }

    @Override
    public Optional<DevGameAsset> findFirstByGameIdAndType(String devGameId, String assetType) {
        return devGameAssetMapper.findFirstByDevGameIdAndAssetType(devGameId, assetType);
    }

    @Override
    public void insert(DevGameAsset asset) {
        if (asset.getUploadedAt() == null) {
            asset.setUploadedAt(LocalDateTime.now());
        }
        devGameAssetMapper.insert(asset);
    }

    @Override
    public void update(DevGameAsset asset) {
        devGameAssetMapper.updateById(asset);
    }

    @Override
    public void save(DevGameAsset asset) {
        if (asset.getId() == null || asset.getId().isEmpty()) {
            asset.setId(UUID.randomUUID().toString());
            asset.setUploadedAt(LocalDateTime.now());
            devGameAssetMapper.insert(asset);
        } else {
            devGameAssetMapper.updateById(asset);
        }
    }


    @Override
    public int batchSave(List<DevGameAsset> assets) {
        LocalDateTime now = LocalDateTime.now();
        assets.forEach(asset -> {
            if (asset.getUploadedAt() == null) {
                asset.setUploadedAt(now);
            }
        });
        return devGameAssetMapper.batchInsert(assets);
    }

    @Override
    public boolean deleteById(String id) {
        return devGameAssetMapper.deleteById(id) > 0;
    }

    @Override
    public void deleteByGameId(String devGameId) {
        devGameAssetMapper.deleteByDevGameId(devGameId);
    }

    @Override
    public long countByGameId(String devGameId) {
        return devGameAssetMapper.countByDevGameId(devGameId);
    }
}
