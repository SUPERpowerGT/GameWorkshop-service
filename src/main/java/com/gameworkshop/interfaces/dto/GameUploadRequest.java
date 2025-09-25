package com.gameworkshop.interfaces.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class GameUploadRequest {
    private String name;
    private String description;
    private LocalDateTime uploadDate;
}
