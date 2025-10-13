package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.DevGameUploadRequest;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequestMapping("/api/developer/devgame")
@RequiredArgsConstructor

public class DevGameUploadController {

    private final DevGameApplicationService devGameApplicationService;
    private final DeveloperProfileRepository developerProfileRepository;

    @PostMapping("/upload")
    public ResponseEntity<DevGameResponse> uploadGame(
            @AuthenticationPrincipal String userId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime releaseDate,
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("video") MultipartFile videoFile,
            @RequestPart("zip") MultipartFile zipFile) {
        DeveloperProfile profile = developerProfileRepository.findByUserId(userId)
                .orElseGet(() -> {
                    String newId = UUID.randomUUID().toString();
                    DeveloperProfile newProfile = new DeveloperProfile(newId, userId, 0);
                    developerProfileRepository.save(newProfile);
                    return newProfile;
                });

        DevGameUploadRequest request = new DevGameUploadRequest();
        request.setDeveloperId(profile.getId());
        request.setName(name);
        request.setDescription(description);
        request.setReleaseDate(releaseDate);
        request.setImage(imageFile);
        request.setVideo(videoFile);
        request.setZip(zipFile);

        DevGameResponse response = devGameApplicationService.uploadGame(request);
        return ResponseEntity.ok(response);
    }
}
