package com.phtransitgraph.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.PlaceRequest;
import com.phtransitgraph.dto.response.PlaceResponse;
import com.phtransitgraph.service.PlaceService;
import jakarta.validation.Valid;

@Tag(name = "Places", description = "Manage and search barangay and municipal places")
@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @Operation(summary = "Get all places")
    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @Operation(summary = "Get a place by ID")
    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable String id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

    @Operation(summary = "Search places by municipality or province")
    @GetMapping("/search")
    public ResponseEntity<List<PlaceResponse>> searchPlaces(
            @RequestParam(required = false) String municipality,
            @RequestParam(required = false) String province) {
        if (municipality != null) {
            return ResponseEntity.ok(placeService.getPlacesByMunicipality(municipality));
        }
        if (province != null) {
            return ResponseEntity.ok(placeService.getPlacesByProvince(province));
        }
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @Operation(summary = "Add a new place (admin only)")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceResponse> createPlace(@Valid @RequestBody PlaceRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(req));
    }

    @Operation(summary = "Update a place (admin only)")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceResponse> updatePlace(
            @PathVariable String id,
            @Valid @RequestBody PlaceRequest req) {
        return ResponseEntity.ok(placeService.updatePlace(id, req));
    }

    @Operation(summary = "Delete a place (admin only)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlace(@PathVariable String id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
