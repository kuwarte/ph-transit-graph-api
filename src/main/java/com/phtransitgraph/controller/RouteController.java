package com.phtransitgraph.controller;

import com.phtransitgraph.dto.request.RouteRequest;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.service.RouteService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    @GetMapping("/search")
    public ResponseEntity<List<RouteResponse>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {
        return ResponseEntity.ok(routeService.searchRoutes(origin, destination));
    }

    @GetMapping("/vehicle/{type}")
    public ResponseEntity<List<RouteResponse>> getRoutesByVehicleType(
            @PathVariable String type) {
        return ResponseEntity.ok(routeService.getRoutesByVehicleType(type));
    }

    @GetMapping("/code/{routeCode}")
    public ResponseEntity<RouteResponse> getRouteByCode(@PathVariable String routeCode) {
        return ResponseEntity.ok(routeService.getRouteByCode(routeCode));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteRequest request) {
        return ResponseEntity.ok(routeService.updateRoute(id, request));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<RouteResponse> updateRouteStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(routeService.updateRouteStatus(id, status));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRoute(@PathVariable String id) {
        routeService.deleteRoute(id);
        return ResponseEntity.noContent().build();
    }
}
