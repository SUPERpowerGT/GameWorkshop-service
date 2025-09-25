package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.interfaces.dto.GameUploadRequest;
import com.gameworkshop.interfaces.dto.GameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/games")
@RequiredArgsConstructor
public class DevGameUploadController {

    private final DevGameApplicationService devGameApplicationService;

    @PostMapping("/upload")
    public ResponseEntity<GameResponse> uploadGame(
            @RequestParam("developerId")String developerId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime releaseDate,
            @RequestPart("image") MultipartFile imageFile,
            @RequestPart("video") MultipartFile videoFile,
            @RequestPart("zip") MultipartFile zipFile) {
        GameUploadRequest request = new GameUploadRequest();
        request.setDeveloperId(developerId);
        request.setName(name);
        request.setDescription(description);
        request.setReleaseDate(releaseDate);
        request.setImage(imageFile);
        request.setVideo(videoFile);
        request.setZip(zipFile);

        GameResponse response = devGameApplicationService.uploadGame(request);
        return ResponseEntity.ok(response);
    }
}
