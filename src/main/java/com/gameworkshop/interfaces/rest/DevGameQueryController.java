package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.application.service.DevGameQueryApplicationService;
import com.gameworkshop.domain.DeveloperProfile.model.DeveloperProfile;
import com.gameworkshop.domain.DeveloperProfile.repository.DeveloperProfileRepository;
import com.gameworkshop.interfaces.dto.DevGameSummaryResponse;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
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

        List<DevGameSummaryResponse> games = devGameQueryApplicationService.listDevGamesWithCover(developerId);
        return ResponseEntity.ok(games);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<DevGameResponse> getGame(@PathVariable String gameId) {
        DevGameResponse game = devGameQueryApplicationService.queryDevGameDetails(gameId);
        if (game != null) {
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
