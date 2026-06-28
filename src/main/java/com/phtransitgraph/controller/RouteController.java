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
import com.phtransitgraph.dto.request.RouteRequest;
import com.phtransitgraph.dto.response.PageResponse;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.service.RouteService;
import jakarta.validation.Valid;

@Tag(name = "Routes", description = "Manage and search transit routes")
@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    @Operation(summary = "Get all routes (paginated)")
    @GetMapping
    public ResponseEntity<PageResponse<RouteResponse>> getAllRoutes(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(routeService.getAllRoutes(page, size));
    }

    @Operation(summary = "Search routes by origin and destination name")
    @GetMapping("/search")
    public ResponseEntity<List<RouteResponse>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {
        return ResponseEntity.ok(routeService.searchRoutes(origin, destination));
    }

    @Operation(summary = "Filter routes by vehicle type (JEEPNEY, BUS, UV_EXPRESS, TRICYCLE)")
    @GetMapping("/vehicle/{type}")
    public ResponseEntity<List<RouteResponse>> getRoutesByVehicleType(
            @PathVariable String type) {
        return ResponseEntity.ok(routeService.getRoutesByVehicleType(type));
    }

    @Operation(summary = "Get a route by its route code")
    @GetMapping("/code/{routeCode}")
    public ResponseEntity<RouteResponse> getRouteByCode(@PathVariable String routeCode) {
        return ResponseEntity.ok(routeService.getRouteByCode(routeCode));
    }

    @Operation(summary = "Get all routes owned by the authenticated operator")
    @GetMapping("/my-routes")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<List<RouteResponse>> getMyRoutes(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(routeService.getMyRoutes(userDetails.getUsername()));
    }

    @Operation(summary = "Get a route by ID")
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getRouteById(id));
    }

    @Operation(summary = "Create a new route (operator only)")
    @PostMapping
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<RouteResponse> createRoute(
            @Valid @RequestBody RouteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(routeService.createRoute(request, userDetails.getUsername()));
    }

    @Operation(summary = "Update a route (operator only, must own the route)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<RouteResponse> updateRoute(
            @PathVariable String id,
            @Valid @RequestBody RouteRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                routeService.updateRoute(id, request, userDetails.getUsername()));
    }

    @Operation(summary = "Change route status (admin only)")
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RouteResponse> updateRouteStatus(
            @PathVariable String id,
            @RequestParam String status) {
        return ResponseEntity.ok(routeService.updateRouteStatus(id, status));
    }

    @Operation(summary = "Deactivate a route (operator only, must own the route)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Void> deleteRoute(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails) {
        routeService.deleteRoute(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
