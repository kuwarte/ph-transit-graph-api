package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.RouteRequest;
import com.phtransitgraph.dto.response.PageResponse;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.service.RouteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<PageResponse<RouteResponse>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(routeService.getAllRoutes(page, size));
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

    @GetMapping("/my-routes")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<RouteResponse>> getMyRoutes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(routeService.getMyRoutes(userDetails.getUsername()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(request, userDetails.getUsername()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                routeService.updateRoute(id, request, userDetails.getUsername()));
    }

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteResponse> updateRouteStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(routeService.updateRouteStatus(id, status));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Void> deleteRoute(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        routeService.deleteRoute(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
