package com.gameworkshop.interfaces.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class GameUploadRequest {
    private String developerId;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private MultipartFile image;
    private MultipartFile video;
    private MultipartFile zip;
}
