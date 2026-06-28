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
import com.phtransitgraph.dto.request.FareRequest;
import com.phtransitgraph.dto.response.FareResponse;
import com.phtransitgraph.service.FareService;
import jakarta.validation.Valid;

@Tag(name = "Fares", description = "Manage fare matrices and calculate fares between stops")
@RestController
@RequestMapping("/api/v1")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @Operation(summary = "Get full fare matrix for a route")
    @GetMapping("/routes/{routeId}/fares")
    public ResponseEntity<List<FareResponse>> getFaresByRoute(@PathVariable String routeId) {
        return ResponseEntity.ok(fareService.getFaresByRouteId(routeId));
    }

    @Operation(summary = "Calculate fare between two stops using their IDs")
    @GetMapping("/fares/calculate")
    public ResponseEntity<FareResponse> calculateFare(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(fareService.calculateFare(from, to));
    }

    @Operation(summary = "Add a fare entry to a route (operator only, must own the route)")
    @PostMapping("/routes/{routeId}/fares")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<FareResponse> addFare(
            @PathVariable String routeId,
            @Valid @RequestBody FareRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fareService.addFare(routeId, req, userDetails.getUsername()));
    }

    @Operation(summary = "Update a fare entry (operator only, must own the route)")
    @PutMapping("/routes/{routeId}/fares/{fareId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<FareResponse> updateFare(
            @PathVariable String routeId,
            @PathVariable String fareId,
            @Valid @RequestBody FareRequest req,
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(
                fareService.updateFare(routeId, fareId, req, userDetails.getUsername()));
    }

    @Operation(summary = "Delete a fare entry (operator only, must own the route)")
    @DeleteMapping("/routes/{routeId}/fares/{fareId}")
    @PreAuthorize("hasRole('OPERATOR')")
    public ResponseEntity<Void> deleteFare(
            @PathVariable String routeId,
            @PathVariable String fareId,
            @AuthenticationPrincipal UserDetails userDetails) {
        fareService.deleteFare(routeId, fareId, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }
}
