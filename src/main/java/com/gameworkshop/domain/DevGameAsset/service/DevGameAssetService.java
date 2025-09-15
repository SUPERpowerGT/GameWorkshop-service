package com.gameworkshop.domain.DevGameAsset.service;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;

import java.util.List;
import java.util.Optional;


public interface DevGameAssetService {

    void uploadAsset(DevGameAsset asset);

    int batchUploadAssets(List<DevGameAsset> assets);

    Optional<DevGameAsset> getAssetById(String id);

    List<DevGameAsset> getAssetsByGameId(String devGameId);

    List<DevGameAsset> getAssetsByGameIdAndType(String devGameId, String assetType);

    boolean deleteAssetById(String id);

    int deleteAssetsByGameId(String devGameId);

    long countAssetsByGameId(String devGameId);
}
