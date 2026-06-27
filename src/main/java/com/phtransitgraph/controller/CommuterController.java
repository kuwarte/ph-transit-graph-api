package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.response.SavedRouteResponse;
import com.phtransitgraph.service.CommuterService;

@RestController
@RequestMapping("/api/v1/commuter")
public class CommuterController {

    private final CommuterService commuterService;

    public CommuterController(CommuterService commuterService) {
        this.commuterService = commuterService;
    }

    // temp
    @GetMapping("/saved-routes")
    public ResponseEntity<List<SavedRouteResponse>> getSavedRoutes(
            @RequestParam String userId) {
        return ResponseEntity.ok(commuterService.getSavedRoutes(userId));
    }

    @PostMapping("/saved-routes/{routeId}")
    public ResponseEntity<SavedRouteResponse> saveRoute(
            @PathVariable String routeId,
            @RequestParam String userId) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commuterService.saveRoute(userId, routeId));
    }

    @DeleteMapping("/saved-routes/{routeId}")
    public ResponseEntity<Void> removeSavedRoute(
            @PathVariable String routeId,
            @RequestParam String userId) {
        commuterService.removeSavedRoute(userId, routeId);
        return ResponseEntity.noContent().build();
    }
}
