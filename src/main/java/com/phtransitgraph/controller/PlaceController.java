package com.phtransitgraph.controller;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.PlaceRequest;
import com.phtransitgraph.dto.response.PlaceResponse;
import com.phtransitgraph.service.PlaceService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/places")
public class PlaceController {

    private final PlaceService placeService;

    public PlaceController(PlaceService placeService) {
        this.placeService = placeService;
    }

    @GetMapping
    public ResponseEntity<List<PlaceResponse>> getAllPlaces() {
        return ResponseEntity.ok(placeService.getAllPlaces());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaceResponse> getPlaceById(@PathVariable String id) {
        return ResponseEntity.ok(placeService.getPlaceById(id));
    }

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

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceResponse> createPlace(@Valid @RequestBody PlaceRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(placeService.createPlace(req));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PlaceResponse> updatePlace(
            @PathVariable String id,
            @Valid @RequestBody PlaceRequest req) {
        return ResponseEntity.ok(placeService.updatePlace(id, req));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePlace(@PathVariable String id) {
        placeService.deletePlace(id);
        return ResponseEntity.noContent().build();
    }
}
