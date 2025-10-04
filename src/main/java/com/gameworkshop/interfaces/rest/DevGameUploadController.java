package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.interfaces.dto.DevGameUploadRequest;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/developer/devgame")
@RequiredArgsConstructor

public class DevGameUploadController {

    private final DevGameApplicationService devGameApplicationService;

    @PostMapping("/upload")
    public ResponseEntity<DevGameResponse> uploadGame(
            @RequestParam("developerId")String developerId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime releaseDate,
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("video") MultipartFile videoFile,
            @RequestPart("zip") MultipartFile zipFile) {
        DevGameUploadRequest request = new DevGameUploadRequest();
        request.setDeveloperId(developerId);
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
