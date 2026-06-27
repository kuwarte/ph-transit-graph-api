package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.FareRequest;
import com.phtransitgraph.dto.response.FareResponse;
import com.phtransitgraph.service.FareService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class FareController {

    private final FareService fareService;

    public FareController(FareService fareService) {
        this.fareService = fareService;
    }

    @GetMapping("/routes/{routeId}/fares")
    public ResponseEntity<List<FareResponse>> getFaresByRoute(@PathVariable String routeId) {
        return ResponseEntity.ok(fareService.getFaresByRouteId(routeId));
    }

    @GetMapping("/fares/calculate")
    public ResponseEntity<FareResponse> calculateFare(
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(fareService.calculateFare(from, to));
    }

    @PostMapping("/routes/{routeId}/fares")
    public ResponseEntity<FareResponse> addFare(
            @PathVariable String routeId,
            @Valid @RequestBody FareRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(fareService.addFare(routeId, req));
    }

    @PutMapping("/routes/{routeId}/fares/{fareId}")
    public ResponseEntity<FareResponse> updateFare(
            @PathVariable String routeId,
            @PathVariable String fareId,
            @Valid @RequestBody FareRequest req) {
        return ResponseEntity.ok(fareService.updateFare(routeId, fareId, req));
    }

    @DeleteMapping("/routes/{routeId}/fares/{fareId}")
    public ResponseEntity<Void> deleteFare(
            @PathVariable String routeId,
            @PathVariable String fareId) {
        fareService.deleteFare(routeId, fareId);
        return ResponseEntity.noContent().build();
    }
}
