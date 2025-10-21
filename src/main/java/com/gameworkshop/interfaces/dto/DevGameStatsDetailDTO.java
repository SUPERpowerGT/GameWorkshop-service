package com.gameworkshop.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevGameStatsDetailDTO {
    private String gameId;
    private String name;
    private int viewCount;
    private int downloadCount;
    private double rating;
    private LocalDateTime updatedAt;
}
