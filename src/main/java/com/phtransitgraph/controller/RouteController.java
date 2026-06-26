package com.phtransitgraph.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.service.RouteService;

@RestController
@RequestMapping("/api/v1/routes")
public class RouteController {

    private final RouteService routeService;

    public RouteController(RouteService routeService) {
        this.routeService = routeService;
    }

    // GET /api/v1/routes
    @GetMapping
    public ResponseEntity<List<RouteResponse>> getAllRoutes() {
        return ResponseEntity.ok(routeService.getAllRoutes());
    }

    // GET /api/v1/routes/search?origin=OriginPlace&destination=DestinationPlace
    @GetMapping("/search")
    public ResponseEntity<List<RouteResponse>> searchRoutes(
            @RequestParam(required = false) String origin,
            @RequestParam(required = false) String destination) {
        return ResponseEntity.ok(routeService.searchResponse(origin, destination));
    }

    // GET /api/v1/routes/vehicle/{type}
    @GetMapping("/vehicle/{type}")
    public ResponseEntity<List<RouteResponse>> getRoutesByVehicleType(
            @PathVariable String type) {
        return ResponseEntity.ok(routeService.getRoutesByVehicleType(type));
    }

    // GET /api/v1/routes/{id}
    @GetMapping("/{id}")
    public ResponseEntity<RouteResponse> getRouteById(
            @PathVariable String id) {
        return routeService.getRouteById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
