package com.gameworkshop.interfaces.rest;

import com.gameworkshop.domain.DevGameAsset.model.DevGameAsset;
import com.gameworkshop.domain.DevGameAsset.repository.DevGameAssetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileNotFoundException;

@RestController
@RequestMapping("/api/developer/devgameasset")
@RequiredArgsConstructor
public class DevGameAssetDownloadController {

    private final DevGameAssetRepository devGameAssetRepository;

    @GetMapping("/download/{assetId}")
    public ResponseEntity<Resource> downloadAsset(@PathVariable String assetId) {
        // 1️⃣ 从数据库查出 asset 信息
        DevGameAsset asset = devGameAssetRepository.findById(assetId)
                .orElseThrow(() -> new IllegalArgumentException("Asset not found"));

        // 2️⃣ 根据文件路径加载文件
        File file = new File(asset.getStoragePath());
        if (!file.exists()) {
            throw new IllegalArgumentException("File not found: " + asset.getStoragePath());
        }

        // 3️⃣ 返回文件流
        Resource resource = new FileSystemResource(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + asset.getFileName() + "\"")
                .contentType(MediaType.parseMediaType(asset.getMimeType()))
                .body(resource);
    }
}
