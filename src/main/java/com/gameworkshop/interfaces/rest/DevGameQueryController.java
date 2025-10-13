package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameQueryApplicationService;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.DevGameSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/developer/devgame")
@RequiredArgsConstructor
public class DevGameQueryController {
    private final DevGameQueryApplicationService devGameQueryApplicationService;
    private final DeveloperProfileRepository developerProfileRepository;

    @GetMapping("/my")
    public ResponseEntity<List<DevGameSummaryResponse>>  getMyGames(@AuthenticationPrincipal String userId) {
        String developerId = developerProfileRepository.findByUserId(userId)
                .map(DeveloperProfile::getId)
                .orElseThrow(() -> new IllegalArgumentException("Developer profile not found"));

        List<DevGameSummaryResponse> games = devGameQueryApplicationService.listGamesWithCoverByDeveloper(developerId);
        return ResponseEntity.ok(games);
    }
}
