package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameStatisticsDashboardService;
import com.gameworkshop.interfaces.dto.DevDashboardDetailedResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developer/dev/statistics")
@RequiredArgsConstructor
public class DevGameStatisticsController {

    private final DevGameStatisticsDashboardService dashboardService;

    @GetMapping("/dashboard/{developerId}")
    public ResponseEntity<DevDashboardDetailedResponse> getDashboardSummary(@PathVariable String developerId) {
        DevDashboardDetailedResponse response = dashboardService.getDashboardDetails(developerId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/by-user/{userId}")
    public ResponseEntity<DevDashboardDetailedResponse> getDashboardByUser(@PathVariable String userId) {
        DevDashboardDetailedResponse response = dashboardService.getDashboardByUserId(userId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard/me")
    public ResponseEntity<DevDashboardDetailedResponse> getMyDashboard(Authentication authentication) {
        if (authentication == null) {
            throw new RuntimeException("Authentication is null!");
        }
        Object principal = authentication.getPrincipal();
        System.out.println(">>> Principal: " + principal + " (class: " + principal.getClass().getName() + ")");
        String userId = principal.toString();
        System.out.println(">>> userId from security context = " + userId);

        DevDashboardDetailedResponse response = dashboardService.getDashboardByUserId(userId);
        return ResponseEntity.ok(response);
    }



}
