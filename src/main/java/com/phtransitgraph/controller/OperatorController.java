package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.OperatorRequest;
import com.phtransitgraph.dto.response.OperatorResponse;
import com.phtransitgraph.dto.response.PageResponse;
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
    public ResponseEntity<PageResponse<OperatorResponse>> getAllVerifiedOperators(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return ResponseEntity.ok(operatorService.getAllVerifiedOperators(page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OperatorResponse> getOperatorById(@PathVariable String id) {
        return ResponseEntity.ok(operatorService.getOperatorById(id));
    }

    @GetMapping("/{id}/routes")
    public ResponseEntity<List<RouteResponse>> getRoutesByOperator(@PathVariable String id) {
        return ResponseEntity.ok(routeService.getRoutesByOperatorId(id));
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<OperatorResponse> updateProfile(
            @Valid @RequestBody OperatorRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                operatorService.updateProfile(userDetails.getUsername(), req));
    }

    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OperatorResponse>> getPendingOperators() {
        return ResponseEntity.ok(operatorService.getPendingOperators());
    }

    @PatchMapping("/{id}/verify")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OperatorResponse> verifyOperator(@PathVariable String id) {
        return ResponseEntity.ok(operatorService.verifyOperator(id));
    }
}
