package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DevGameAssetApplicationService {
    private final DevGameAssetRepository devGameAssetRepository;

    public Optional<DevGameAsset> getAssetById(String id) {
        return devGameAssetRepository.findById(id);
    }
}
