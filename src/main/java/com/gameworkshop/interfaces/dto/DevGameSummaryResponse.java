package com.gameworkshop.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
//用于展示游戏名称，游戏描述，游戏图片
@Data
@AllArgsConstructor
public class DevGameSummaryResponse {
    private String id;
    private String name;
    private String description;
    private LocalDateTime releaseDate;
    private LocalDateTime createdAt;
    private String imageUrl;
}
