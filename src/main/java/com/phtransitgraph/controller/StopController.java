package com.phtransitgraph.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.phtransitgraph.dto.request.StopReorderRequest;
import com.phtransitgraph.dto.request.StopRequest;
import com.phtransitgraph.dto.response.StopResponse;
import com.phtransitgraph.service.StopService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/routes/{routeId}/stops")
public class StopController {
    private final StopService stopService;

    public StopController(StopService stopService) {
        this.stopService = stopService;
    }

    @GetMapping
    public ResponseEntity<List<StopResponse>> getAllStops(@PathVariable String routeId) {
        return ResponseEntity.ok(stopService.getAllStopsByRouteId(routeId));
    }

    @GetMapping("/{stopId}")
    public ResponseEntity<StopResponse> getStopById(
            @PathVariable String routeId,
            @PathVariable String stopId) {
        return ResponseEntity.ok(stopService.getStopById(routeId, stopId));
    }

    @PostMapping
    public ResponseEntity<StopResponse> addStop(
            @PathVariable String routeId,
            @Valid @RequestBody StopRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(stopService.addStop(routeId, req));
    }

    @PutMapping("/{stopId}")
    public ResponseEntity<StopResponse> updateStop(
            @PathVariable String routeId,
            @PathVariable String stopId,
            @Valid @RequestBody StopRequest req) {
        return ResponseEntity.ok(stopService.updateStop(routeId, stopId, req));
    }

    @DeleteMapping("/{stopId}")
    public ResponseEntity<Void> deleteStop(
            @PathVariable String routeId,
            @PathVariable String stopId) {
        stopService.deleteStop(routeId, stopId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/reorder")
    public ResponseEntity<List<StopResponse>> reorderStops(
            @PathVariable String routeId,
            @Valid @RequestBody StopReorderRequest req) {
        return ResponseEntity.ok(stopService.reorderStops(routeId, req));
    }
}
