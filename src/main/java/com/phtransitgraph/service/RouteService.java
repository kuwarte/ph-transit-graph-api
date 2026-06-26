package com.phtransitgraph.service;

import com.phtransitgraph.dto.request.RouteRequest;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.entity.Place;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.enums.RouteStatus;
import com.phtransitgraph.enums.VehicleType;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.PlaceRepository;
import com.phtransitgraph.repository.RouteRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PlaceRepository placeRepository;

    public RouteService(RouteRepository routeRepository, PlaceRepository placeRepository) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
    }

    private RouteResponse toResponse(Route route) {
        return new RouteResponse(
                route.getId(),
                route.getRouteCode(),
                route.getRouteName(),
                route.getOrigin().getName(),
                route.getOrigin().getMunicipality(),
                route.getDestination().getName(),
                route.getDestination().getMunicipality(),
                route.getVehicleType().name(),
                route.getStatus().name(),
                route.getCreatedAt(),
                route.getUpdatedAt());
    }

    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RouteResponse getRouteById(String id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        return toResponse(route);
    }

    public RouteResponse getRouteByCode(String routeCode) {
        Route route = routeRepository.findByRouteCode(routeCode)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with code: " + routeCode));
        return toResponse(route);
    }

    public List<RouteResponse> searchRoutes(String origin, String destination) {
        String o = origin == null ? "" : origin;
        String d = destination == null ? "" : destination;
        return routeRepository.searchByOriginAndDestination(o, d)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RouteResponse> getRoutesByVehicleType(String type) {
        VehicleType vehicleType = VehicleType.valueOf(type.toUpperCase());
        return routeRepository.findByVehicleType(vehicleType)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public RouteResponse createRoute(RouteRequest request) {
        routeRepository.findByRouteCode(request.getRouteCode())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Route with code '" + request.getRouteCode() + "' already exists");
                });

        Place origin = placeRepository.findById(request.getOriginId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Origin place not found with id: " + request.getOriginId()));

        Place destination = placeRepository.findById(request.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination place not found with id: " + request.getDestinationId()));

        Route route = Route.builder()
                .routeCode(request.getRouteCode())
                .routeName(request.getRouteName())
                .origin(origin)
                .destination(destination)
                .vehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()))
                .status(RouteStatus.ACTIVE)
                .build();

        return toResponse(routeRepository.save(route));
    }

    public RouteResponse updateRoute(String id, RouteRequest request) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));

        Place origin = placeRepository.findById(request.getOriginId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Origin place not found with id: " + request.getOriginId()));

        Place destination = placeRepository.findById(request.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination place not found with id: " + request.getDestinationId()));

        route.setRouteCode(request.getRouteCode());
        route.setRouteName(request.getRouteName());
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setVehicleType(VehicleType.valueOf(request.getVehicleType().toUpperCase()));

        return toResponse(routeRepository.save(route));
    }

    public RouteResponse updateRouteStatus(String id, String status) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        route.setStatus(RouteStatus.valueOf(status.toUpperCase()));
        return toResponse(routeRepository.save(route));
    }

    public void deleteRoute(String id) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        routeRepository.delete(route);
    }
}
