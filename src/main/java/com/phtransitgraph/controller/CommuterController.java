package com.phtransitgraph.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.response.SavedRouteResponse;
import com.phtransitgraph.service.CommuterService;

@Tag(name = "Commuter", description = "Manage saved routes for authenticated commuters")
@RestController
@RequestMapping("/api/v1/commuter")
@PreAuthorize("isAuthenticated()")
public class CommuterController {

    private final CommuterService commuterService;

    public CommuterController(CommuterService commuterService) {
        this.commuterService = commuterService;
    }

    @Operation(summary = "Get all saved routes for the authenticated user")
    @GetMapping("/saved-routes")
    public ResponseEntity<List<SavedRouteResponse>> getSavedRoutes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                commuterService.getSavedRoutes(userDetails.getUsername()));
    }

    @Operation(summary = "Save a route to the authenticated user's bookmarks")
    @PostMapping("/saved-routes/{routeId}")
    public ResponseEntity<SavedRouteResponse> saveRoute(
            @PathVariable String routeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commuterService.saveRoute(userDetails.getUsername(), routeId));
    }

    @Operation(summary = "Remove a route from the authenticated user's bookmarks")
    @DeleteMapping("/saved-routes/{routeId}")
    public ResponseEntity<Void> removeSavedRoute(
            @PathVariable String routeId,
            @AuthenticationPrincipal UserDetails userDetails) {
        commuterService.removeSavedRoute(userDetails.getUsername(), routeId);
        return ResponseEntity.noContent().build();
    }
}
