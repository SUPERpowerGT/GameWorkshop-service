package com.gameworkshop.interfaces.rest;

import com.gameworkshop.application.service.DevGameApplicationService;
import com.gameworkshop.interfaces.dto.OperationResult;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/developer/devgame")
@RequiredArgsConstructor
public class DevGameDeleteController {

    private final DevGameApplicationService devGameApplicationService;

    @DeleteMapping("/{gameId}")
    public ResponseEntity<OperationResult> deleteGame(@PathVariable String gameId) {
        OperationResult result = devGameApplicationService.deleteGame(gameId);

        if (result.isSuccess()) {
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(result);
        }
    }
}
