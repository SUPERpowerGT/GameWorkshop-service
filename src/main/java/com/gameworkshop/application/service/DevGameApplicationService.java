package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.DevGameUploadRequest;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import com.gameworkshop.interfaces.dto.OperationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DevGameApplicationService {
    @Value("${app.asset-storage-path}")
    private String assetStoragePath;
    private final DevGameRepository devGameRepository;
    private final DevGameAssetRepository devGameAssetRepository;
    private final DeveloperProfileRepository developerProfileRepository;

    public DevGameResponse uploadGame(DevGameUploadRequest request) {
        DeveloperProfile developer = developerProfileRepository.findById(request.getDeveloperId())
                .orElseThrow(() -> new IllegalArgumentException("Developer profile not found"));

        String gameId = UUID.randomUUID().toString();

        DevGame game = new DevGame(
                gameId,
                request.getDeveloperId(),
                request.getName(),
                request.getDescription(),
                request.getReleaseDate(),
                LocalDateTime.now(),
                LocalDateTime.now()
        );
        devGameRepository.insert(game);

        String imageUrl = saveAsset(developer.getUserId(), request.getName(), gameId, request.getImage(), "image");
        String videoUrl = saveAsset(developer.getUserId(), request.getName(), gameId, request.getVideo(), "video");
        String zipUrl   = saveAsset(developer.getUserId(), request.getName(), gameId, request.getZip(), "zip");


        developerProfileRepository.syncProjectCount(request.getDeveloperId());

        return new DevGameResponse(gameId, request.getName(), request.getDescription(),
                imageUrl, videoUrl, zipUrl);
    }

    private String saveAsset(String userId, String gameName, String gameId, MultipartFile file, String assetType) {
        try {
            String safeUserId = sanitizePathSegment(userId);
            String safeGameName = sanitizePathSegment(gameName);

            String basePath = assetStoragePath + safeUserId + "/" + safeGameName + "/";
            Path folder = Paths.get(basePath);
            Files.createDirectories(folder);

            String fileName = file.getOriginalFilename();
            String storagePath = basePath + fileName;
            File dest = new File(storagePath);
            file.transferTo(dest);

            DevGameAsset asset = new DevGameAsset(
                    UUID.randomUUID().toString(),
                    gameId,
                    assetType,
                    fileName,
                    storagePath,
                    file.getSize(),
                    file.getContentType(),
                    LocalDateTime.now()
            );
            devGameAssetRepository.insert(asset);

            return "/assets/" + safeUserId + "/" + safeGameName + "/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save asset: " + assetType, e);
        }
    }

    @Transactional
    public OperationResult deleteGame(String gameId) {
        if (!devGameRepository.existsById(gameId)) {
            return OperationResult.failure("Game not found: " + gameId);
        }

        try {
            // 删除关联资源
            devGameAssetRepository.deleteByGameId(gameId);

            // 删除游戏本体
            devGameRepository.deleteById(gameId);

            return OperationResult.success("Game deleted successfully: " + gameId);
        } catch (Exception e) {
            return OperationResult.failure("Failed to delete game: " + e.getMessage());
        }
    }


    private String sanitizePathSegment(String input) {
        return input.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

}
