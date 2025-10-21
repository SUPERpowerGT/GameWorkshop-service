package com.gameworkshop.interfaces.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DevDashboardDetailedResponse {

    private String developerId;

    private Summary summary;

    private List<DevGameStatsDetailDTO> games;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Summary {
        private int totalGames;
        private int totalViews;
        private int totalDownloads;
        private double averageRating;
    }
}
