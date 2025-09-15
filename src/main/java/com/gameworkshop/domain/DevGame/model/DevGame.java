package com.gameworkshop.domain.DevGame.model;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DevGame {
    private String id;
    private String developerProfileId;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
