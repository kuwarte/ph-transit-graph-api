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
import com.phtransitgraph.repository.UserRepository;

@Service
public class CommuterService {

    private final SavedRouteRepository savedRouteRepository;
    private final UserRepository userRepository;
    private final RouteRepository routeRepository;

    public CommuterService(SavedRouteRepository savedRouteRepository,
            UserRepository userRepository, RouteRepository routeRepository) {
        this.savedRouteRepository = savedRouteRepository;
        this.userRepository = userRepository;
        this.routeRepository = routeRepository;
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

    private User findUserOrThrow(String userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + userId));
    }

    public List<SavedRouteResponse> getSavedRoutes(String userId) {
        findUserOrThrow(userId);
        return savedRouteRepository.findByUserId(userId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public SavedRouteResponse saveRoute(String userId, String routeId) {
        User user = findUserOrThrow(userId);
        Route route = routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + routeId));

        if (savedRouteRepository.existsByUserIdAndRouteId(userId, routeId)) {
            throw new DuplicateResourceException("Route is already saved");
        }

        SavedRoute savedRoute = SavedRoute.builder()
                .user(user)
                .route(route)
                .build();

        return toResponse(savedRouteRepository.save(savedRoute));
    }

    public void removeSavedRoute(String userId, String routeId) {
        findUserOrThrow(userId);
        SavedRoute savedRoute = savedRouteRepository
                .findByUserIdAndRouteId(userId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Saved route not found for this user and route"));
        savedRouteRepository.delete(savedRoute);
    }
}
