package com.gameworkshop.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotGameResponse {
    private String id;
    private String name;
    private String description;
    private String coverImageUrl;
    private double score; // 热度分数
}
