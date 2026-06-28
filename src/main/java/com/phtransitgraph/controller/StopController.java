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
import com.phtransitgraph.dto.request.StopReorderRequest;
import com.phtransitgraph.dto.request.StopRequest;
import com.phtransitgraph.dto.response.StopResponse;
import com.phtransitgraph.service.StopService;
import jakarta.validation.Valid;

@Tag(name = "Stops", description = "Manage stops along a transit route")
@RestController
@RequestMapping("/api/v1/routes/{routeId}/stops")
public class StopController {

    private final StopService stopService;

    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    @Operation(summary = "Get all stops for a route ordered by sequence")
    @GetMapping
    public ResponseEntity<List<StopResponse>> getAllStops(@PathVariable String routeId) {
        return ResponseEntity.ok(stopService.getAllStopsByRouteId(routeId));
    }

    @Operation(summary = "Get a specific stop on a route")
    @GetMapping("/{stopId}")
    public ResponseEntity<StopResponse> getStopById(
            @PathVariable String routeId,
            @PathVariable String stopId) {
        return ResponseEntity.ok(stopService.getStopById(routeId, stopId));
    }

    @Operation(summary = "Add a stop to a route (operator only, must own the route)")
    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<StopResponse> addStop(
            @PathVariable String routeId,
            @Valid @RequestBody StopRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stopService.addStop(routeId, req, userDetails.getUsername()));
    }

    @Operation(summary = "Update a stop (operator only, must own the route)")
    @PutMapping("/{stopId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<StopResponse> updateStop(
            @PathVariable String routeId,
            @PathVariable String stopId,
            @Valid @RequestBody StopRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                stopService.updateStop(routeId, stopId, req, userDetails.getUsername()));
    }

    @Operation(summary = "Remove a stop from a route (operator only, must own the route)")
    @DeleteMapping("/{stopId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Void> deleteStop(
            @PathVariable String routeId,
            @PathVariable String stopId,
            @AuthenticationPrincipal UserDetails userDetails) {
        stopService.deleteStop(routeId, stopId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Reorder stops on a route (operator only, must own the route)")
    @PatchMapping("/reorder")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<StopResponse>> reorderStops(
            @PathVariable String routeId,
            @Valid @RequestBody StopReorderRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                stopService.reorderStops(routeId, req, userDetails.getUsername()));
    }
}
