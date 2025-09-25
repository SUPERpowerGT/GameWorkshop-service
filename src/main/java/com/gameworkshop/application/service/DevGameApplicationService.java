package com.gameworkshop.application.service;

import com.gameworkshop.domain.DevGame.model.DevGame;
import com.gameworkshop.domain.DevGame.repository.DevGameRepository;
import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.GameUploadRequest;
import com.gameworkshop.interfaces.dto.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
    private final DevGameRepository devGameRepository;
    private final DevGameAssetRepository devGameAssetRepository;
    private final DeveloperProfileRepository developerProfileRepository;

    public GameResponse uploadGame(GameUploadRequest request) {
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

        return new GameResponse(gameId, request.getName(), request.getDescription(),
                imageUrl, videoUrl, zipUrl);
    }

    private String saveAsset(String userId, String gameName, String gameId, MultipartFile file, String assetType) {
        try {
            String safeUserId = sanitizePathSegment(userId);
            String safeGameName = sanitizePathSegment(gameName);

            String basePath = "D:/Project/GameValutProject/game-assets/" + safeUserId + "/" + safeGameName + "/";
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

    private String sanitizePathSegment(String input) {
        return input.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

}
