package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameQueryApplicationService;
import com.gameworkshop.application.service.DevGameStatisticsApplicationService;
import com.gameworkshop.interfaces.dto.DevGameListResponse;
import com.gameworkshop.interfaces.dto.DevGameResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developer/devgame/public")
@RequiredArgsConstructor
public class DevGamePublicController {

    private final DevGameQueryApplicationService devGameQueryApplicationService;
    private final DevGameStatisticsApplicationService devGameStatisticsApplicationService;

    /**
     * GameHub 公共游戏列表（分页）
     */
    @GetMapping("/all")
    public ResponseEntity<DevGameListResponse> listAllGames(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "12") int pageSize
    ) {
        DevGameListResponse result = devGameQueryApplicationService.listAllGames(page, pageSize);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{gameId}")
    public ResponseEntity<DevGameResponse> getPublicGameDetail(@PathVariable String gameId) {
        DevGameResponse game = devGameQueryApplicationService.queryDevGameDetails(gameId);
        if (game != null) {
            devGameStatisticsApplicationService.recordGameView(gameId);
            return ResponseEntity.ok(game);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
