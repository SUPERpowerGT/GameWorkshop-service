package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/developer/devgame")
@RequiredArgsConstructor
public class DevGameUpdateController {

    private final DevGameApplicationService devGameApplicationService;

    @PutMapping("/update/{gameId}")
    public ResponseEntity<DevGameResponse> updateGame(
            @PathVariable String gameId,
            @RequestParam("name") String name,
            @RequestParam("description") String description,
            @RequestParam(value = "releaseDate", required = false) String releaseDate,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "video", required = false) MultipartFile video,
            @RequestPart(value = "zip", required = false) MultipartFile zip
    ) {
        DevGameResponse response = devGameApplicationService.updateGame(
                gameId, name, description, releaseDate, image, video, zip
        );
        return ResponseEntity.ok(response);
    }
}
