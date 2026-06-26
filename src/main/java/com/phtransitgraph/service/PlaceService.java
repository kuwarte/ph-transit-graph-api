package com.phtransitgraph.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.request.PlaceRequest;
import com.phtransitgraph.dto.response.PlaceResponse;
import com.phtransitgraph.entity.Place;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.PlaceRepository;

@Service
public class PlaceService {
    private final PlaceRepository placeRepository;

    public PlaceService(PlaceRepository placeRepository) {
        this.placeRepository = placeRepository;
    }

    private PlaceResponse toResponse(Place place) {
        return new PlaceResponse(
                place.getId(),
                place.getName(),
                place.getMunicipality(),
                place.getProvince(),
                place.getLatitude(),
                place.getLongitude(),
                place.getCreatedAt());
    }

    public List<PlaceResponse> getAllPlaces() {
        return placeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlaceResponse getPlaceById(String id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found with id + " + id));

        return toResponse(place);

    }

    public List<PlaceResponse> getPlacesByMunicipality(String municipality) {
        return placeRepository.findByMunicipalityIgnoreCase(municipality)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<PlaceResponse> getPlacesByProvince(String province) {
        return placeRepository.findByProvinceIgnoreCase(province)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public PlaceResponse createPlace(PlaceRequest req) {
        placeRepository.findByNameIgnoreCaseAndMunicipalityIgnoreCase(req.getName(), req.getMunicipality())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Place '" + req.getName() + "' already exists in " + req.getMunicipality());
                });

        Place place = Place.builder()
                .name(req.getName())
                .municipality(req.getMunicipality())
                .province(req.getProvince())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .build();

        return toResponse(placeRepository.save(place));
    }

    public PlaceResponse updatePlace(String id, PlaceRequest req) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found in id " + id));

        place.setName(req.getName());
        place.setMunicipality(req.getMunicipality());
        place.setProvince(req.getProvince());
        place.setLatitude(req.getLatitude());
        place.setLongitude(req.getLongitude());

        return toResponse(placeRepository.save(place));
    }

    public void deletePlace(String id) {
        Place place = placeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Place not found in id " + id));

        placeRepository.delete(place);
    }
}
