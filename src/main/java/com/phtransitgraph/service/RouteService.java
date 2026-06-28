package com.phtransitgraph.service;

import com.phtransitgraph.dto.request.RouteRequest;
import com.phtransitgraph.dto.response.PageResponse;
import com.phtransitgraph.dto.response.RouteResponse;
import com.phtransitgraph.entity.Operator;
import com.phtransitgraph.entity.Place;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.enums.RouteStatus;
import com.phtransitgraph.enums.VehicleType;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.PlaceRepository;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.security.OwnershipValidator;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RouteService {

    private final RouteRepository routeRepository;
    private final PlaceRepository placeRepository;
    private final OwnershipValidator ownershipValidator;

    public RouteService(RouteRepository routeRepository, PlaceRepository placeRepository,
            OwnershipValidator ownershipValidator) {
        this.routeRepository = routeRepository;
        this.placeRepository = placeRepository;
        this.ownershipValidator = ownershipValidator;
    }

    RouteResponse toResponse(Route route) {
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
                route.getOperator() != null ? route.getOperator().getId() : null,
                route.getOperator() != null ? route.getOperator().getOperatorName() : null,
                route.getCreatedAt(),
                route.getUpdatedAt());
    }

    public PageResponse<RouteResponse> getAllRoutes(int page, int size) {
        Page<Route> result = routeRepository.findAll(PageRequest.of(page, size));
        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());
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

    public List<RouteResponse> getRoutesByOperatorId(String operatorId) {
        return routeRepository.findByOperatorId(operatorId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public List<RouteResponse> getMyRoutes(String email) {
        Operator operator = ownershipValidator.getOperatorFromEmail(email);
        return routeRepository.findByOperatorId(operator.getId())
                .stream().map(this::toResponse).toList();
    }

    public RouteResponse createRoute(RouteRequest req, String email) {
        routeRepository.findByRouteCode(req.getRouteCode())
                .ifPresent(existing -> {
                    throw new DuplicateResourceException(
                            "Route with code '" + req.getRouteCode() + "' already exists");
                });
        Place origin = placeRepository.findById(req.getOriginId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Origin place not found with id: " + req.getOriginId()));
        Place destination = placeRepository.findById(req.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination place not found with id: " + req.getDestinationId()));
        Operator operator = ownershipValidator.getOperatorFromEmail(email);

        Route route = Route.builder()
                .routeCode(req.getRouteCode())
                .routeName(req.getRouteName())
                .origin(origin)
                .destination(destination)
                .vehicleType(VehicleType.valueOf(req.getVehicleType().toUpperCase()))
                .status(RouteStatus.ACTIVE)
                .operator(operator)
                .build();
        return toResponse(routeRepository.save(route));
    }

    public RouteResponse updateRoute(String id, RouteRequest req, String email) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        ownershipValidator.assertOwnsRoute(route, email);

        Place origin = placeRepository.findById(req.getOriginId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Origin place not found with id: " + req.getOriginId()));
        Place destination = placeRepository.findById(req.getDestinationId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Destination place not found with id: " + req.getDestinationId()));

        route.setRouteCode(req.getRouteCode());
        route.setRouteName(req.getRouteName());
        route.setOrigin(origin);
        route.setDestination(destination);
        route.setVehicleType(VehicleType.valueOf(req.getVehicleType().toUpperCase()));
        return toResponse(routeRepository.save(route));
    }

    public RouteResponse updateRouteStatus(String id, String status) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        route.setStatus(RouteStatus.valueOf(status.toUpperCase()));
        return toResponse(routeRepository.save(route));
    }

    public void deleteRoute(String id, String email) {
        Route route = routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
        ownershipValidator.assertOwnsRoute(route, email);
        routeRepository.delete(route);
    }
}
