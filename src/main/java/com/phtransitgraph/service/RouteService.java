package com.phtransitgraph.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.enums.VehicleType;
import com.phtransitgraph.repository.RouteRepository;

@Service
public class RouteService {

    private final RouteRepository routeRepository;

    public RouteService(RouteRepository routeRepository) {
        this.routeRepository = routeRepository;
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
                route.getStatus().name());
    }

    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public Optional<RouteResponse> getRouteById(String id) {
        return routeRepository.findById(id)
                .map(this::toResponse);
    }

    public List<RouteResponse> searchResponse(String origin, String destination) {
        String o = origin == null ? "" : origin;
        String d = destination == null ? "" : destination;

        return routeRepository
                .findByOriginContainingIgnoreCaseAndDestinationContainingIgnoreCase(o, d)
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

}
