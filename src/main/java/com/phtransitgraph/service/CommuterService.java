package com.phtransitgraph.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.phtransitgraph.dto.response.SavedRouteResponse;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.entity.SavedRoute;
import com.phtransitgraph.entity.User;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.repository.SavedRouteRepository;
import com.phtransitgraph.security.OwnershipValidator;

@Service
public class CommuterService {

    private final SavedRouteRepository savedRouteRepository;
    private final RouteRepository routeRepository;
    private final OwnershipValidator ownershipValidator;

    public CommuterService(SavedRouteRepository savedRouteRepository,
            RouteRepository routeRepository, OwnershipValidator ownershipValidator) {
        this.savedRouteRepository = savedRouteRepository;
        this.routeRepository = routeRepository;
        this.ownershipValidator = ownershipValidator;
    }

    private SavedRouteResponse toResponse(SavedRoute savedRoute) {
        Route route = savedRoute.getRoute();
        return new SavedRouteResponse(
                savedRoute.getId(),
                savedRoute.getUser().getId(),
                route.getId(),
                route.getRouteCode(),
                route.getRouteName(),
                route.getOrigin().getName(),
                route.getDestination().getName(),
                route.getVehicleType().name(),
                savedRoute.getSavedAt());
    }

    public List<SavedRouteResponse> getSavedRoutes(String email) {
        User user = ownershipValidator.getUserFromEmail(email);
        return savedRouteRepository.findByUserId(user.getId())
                .stream().map(this::toResponse).toList();
    }

    public SavedRouteResponse saveRoute(String email, String routeId) {
        User user = ownershipValidator.getUserFromEmail(email);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + routeId));
        if (savedRouteRepository.existsByUserIdAndRouteId(user.getId(), routeId)) {
            throw new DuplicateResourceException("Route is already saved");
        }
        SavedRoute savedRoute = SavedRoute.builder()
                .user(user)
                .route(route)
                .build();
        return toResponse(savedRouteRepository.save(savedRoute));
    }

    public void removeSavedRoute(String email, String routeId) {
        User user = ownershipValidator.getUserFromEmail(email);
        SavedRoute savedRoute = savedRouteRepository
                .findByUserIdAndRouteId(user.getId(), routeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Saved route not found for this user and route"));
        savedRouteRepository.delete(savedRoute);
    }
}
