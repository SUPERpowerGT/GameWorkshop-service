package com.gameworkshop.domain.DevGameAsset.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DevGameAsset {
    private String id;
    private String devGameId;
    private String assetType;
    private String fileName;
    private String storagePath;
    private Long fileSize;
    private String mimeType;
    private LocalDateTime uploadedAt;
}
