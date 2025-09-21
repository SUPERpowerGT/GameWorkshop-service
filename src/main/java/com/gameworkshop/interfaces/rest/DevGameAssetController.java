package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameAssetApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
@RequestMapping("/api/assets")
@RequiredArgsConstructor
public class DevGameAssetController {
    private final DevGameAssetApplicationService devGameAssetApplicationService;

    @GetMapping("/download/{id}")
    public ResponseEntity<FileSystemResource> download(@PathVariable("id") String id) {
        return devGameAssetApplicationService.getAssetById(id)
                .filter(devGameAsset -> new File(devGameAsset.getStoragePath()).exists())
                .map(devGameAsset -> {
                    File file = new File(devGameAsset.getStoragePath());
                    FileSystemResource resource = new FileSystemResource(file);
                    return ResponseEntity.ok()
                            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + devGameAsset.getFileName())
                            .contentType(MediaType.parseMediaType(devGameAsset.getMimeType()))
                            .contentLength(devGameAsset.getFileSize())
                            .body(resource);

                })
                .orElse(ResponseEntity.notFound().build());
    }

}
