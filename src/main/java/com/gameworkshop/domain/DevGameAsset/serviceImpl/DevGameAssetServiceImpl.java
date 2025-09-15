package com.gameworkshop.domain.DevGameAsset.serviceImpl;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.domain.DevGameAsset.service.DevGameAssetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevGameAssetServiceImpl implements DevGameAssetService {

    private final DevGameAssetRepository devGameAssetRepository;

    @Override
    public void uploadAsset(DevGameAsset asset) {
        validateAsset(asset);

        devGameAssetRepository.save(asset);
    }

    @Override
    public int batchUploadAssets(List<DevGameAsset> assets) {
        if (assets.isEmpty()) {
            throw new IllegalArgumentException("upload assets cloud not be empty");
        }

        String firstGameId = assets.get(0).getDevGameId();
        assets.forEach(asset -> {
            validateAsset(asset);
            if (!firstGameId.equals(asset.getDevGameId())) {
                throw new IllegalArgumentException("The resources uploaded in batches must belong to the same game.");
            }
        });

        return devGameAssetRepository.batchSave(assets);
    }

    @Override
    public Optional<DevGameAsset> getAssetById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("The resource ID cannot be empty.");
        }
        return devGameAssetRepository.findById(id);
    }

    @Override
    public List<DevGameAsset> getAssetsByGameId(String devGameId) {
        if (devGameId == null || devGameId.trim().isEmpty()) {
            throw new IllegalArgumentException("The Game ID cannot be empty.");
        }
        return devGameAssetRepository.findByGameId(devGameId);
    }

    @Override
    public List<DevGameAsset> getAssetsByGameIdAndType(String devGameId, String assetType) {
        if (devGameId == null || devGameId.trim().isEmpty()) {
            throw new IllegalArgumentException("The Game ID cannot be empty.");
        }
        if (assetType == null || assetType.trim().isEmpty()) {
            throw new IllegalArgumentException("The Asset Type cannot be empty.");
        }
        return devGameAssetRepository.findByGameIdAndType(devGameId, assetType);
    }

    @Override
    public boolean deleteAssetById(String id) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("The resource ID cannot be empty.");
        }
        return devGameAssetRepository.deleteById(id);
    }

    @Override
    public int deleteAssetsByGameId(String devGameId) {
        if (devGameId == null || devGameId.trim().isEmpty()) {
            throw new IllegalArgumentException("The Game ID cannot be empty.");
        }
        return devGameAssetRepository.deleteByGameId(devGameId);
    }

    @Override
    public long countAssetsByGameId(String devGameId) {
        if (devGameId == null || devGameId.trim().isEmpty()) {
            throw new IllegalArgumentException("The Game ID cannot be empty.");
        }
        return devGameAssetRepository.countByGameId(devGameId);
    }

    private void validateAsset(DevGameAsset asset) {
        if (asset.getDevGameId() == null || asset.getDevGameId().trim().isEmpty()) {
            throw new IllegalArgumentException("The Asset ID cannot be empty.");
        }
        if (asset.getFileName() == null || asset.getFileName().trim().isEmpty()) {
            throw new IllegalArgumentException("The file name cannot be empty.");
        }
        if (asset.getStoragePath() == null || asset.getStoragePath().trim().isEmpty()) {
            throw new IllegalArgumentException("The storage path cannot be empty.");
        }
    }
}
