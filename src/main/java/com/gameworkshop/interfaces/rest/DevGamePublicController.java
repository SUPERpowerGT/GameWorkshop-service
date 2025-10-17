package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameQueryApplicationService;
import com.gameworkshop.interfaces.dto.DevGameListResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developer/devgame/public")
@RequiredArgsConstructor
public class DevGamePublicController {

    private final DevGameQueryApplicationService devGameQueryApplicationService;

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
}
