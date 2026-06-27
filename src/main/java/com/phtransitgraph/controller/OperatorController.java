package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.OperatorRequest;
import com.phtransitgraph.dto.response.OperatorResponse;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.service.OperatorService;
import com.phtransitgraph.service.RouteService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/operators")
public class OperatorController {

    private final OperatorService operatorService;
    private final RouteService routeService;

    public OperatorController(OperatorService operatorService, RouteService routeService) {
        this.operatorService = operatorService;
        this.routeService = routeService;
    }

    @GetMapping
    public ResponseEntity<List<OperatorResponse>> getAllVerifiedOperators() {
        return ResponseEntity.ok(operatorService.getAllVerifiedOperators());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperatorResponse> getOperatorById(@PathVariable String id) {
        return ResponseEntity.ok(operatorService.getOperatorById(id));
    }

    @GetMapping("/{id}/routes")
    public ResponseEntity<List<RouteResponse>> getRoutesByOperator(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getRoutesByOperatorId(id));
    }

    // temp
    @PutMapping("/{id}/profile")
    public ResponseEntity<OperatorResponse> updateProfile(
            @PathVariable String id,
            @Valid @RequestBody OperatorRequest req) {
        return ResponseEntity.ok(operatorService.updateProfile(id, req));
    }

    // temp
    @GetMapping("/pending")
    public ResponseEntity<List<OperatorResponse>> getPendingOperators() {
        return ResponseEntity.ok(operatorService.getPendingOperators());
    }

    // temp
    @PatchMapping("/{id}/verify")
    public ResponseEntity<OperatorResponse> verifyOperator(@PathVariable String id) {
        return ResponseEntity.ok(operatorService.verifyOperator(id));
    }
}
