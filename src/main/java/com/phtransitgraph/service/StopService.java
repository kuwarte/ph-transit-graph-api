package com.phtransitgraph.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.request.StopReorderItem;
import com.phtransitgraph.dto.request.StopReorderRequest;
import com.phtransitgraph.dto.request.StopRequest;
import com.phtransitgraph.dto.response.StopResponse;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.entity.Stop;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.repository.StopRepository;

import jakarta.transaction.Transactional;

@Service
public class StopService {
    private final StopRepository stopRepository;
    private final RouteRepository routeRepository;

    public StopService(StopRepository stopRepository, RouteRepository routeRepository) {
        this.stopRepository = stopRepository;
        this.routeRepository = routeRepository;
    }

    private StopResponse toResponse(Stop stop) {
        return new StopResponse(
                stop.getId(),
                stop.getRoute().getId(),
                stop.getStopName(),
                stop.getSequenceOrder(),
                stop.getLatitude(),
                stop.getLongitude(),
                stop.getLandmark(),
                stop.getCreatedAt());
    }

    private Route findRouterOrThrow(String routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id " + routeId));

    }

    public List<StopResponse> getAllStopsByRouteId(String routeId) {
        findRouterOrThrow(routeId);
        return stopRepository.findByRouteIdOrderBySequenceOrderAsc(routeId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public StopResponse getStopById(String routeId, String stopId) {
        findRouterOrThrow(routeId);
        Stop stop = stopRepository.findByIdAndRouteId(stopId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found with id " + stopId));
        return toResponse(stop);
    }

    public StopResponse addStop(String routeId, StopRequest req) {
        Route route = findRouterOrThrow(routeId);
        if (stopRepository.existsByRouteIdAndSequenceOrder(routeId, req.getSequenceOrder())) {
            throw new DuplicateResourceException(
                    "A stop with sequence order " + req.getSequenceOrder() + " already exists on this route");
        }

        Stop stop = Stop.builder()
                .route(route)
                .stopName(req.getStopName())
                .sequenceOrder(req.getSequenceOrder())
                .latitude(req.getLatitude())
                .longitude(req.getLongitude())
                .landmark(req.getLandmark())
                .build();

        return toResponse(stopRepository.save(stop));
    }

    public StopResponse updateStop(String routeId, String stopId, StopRequest req) {
        findRouterOrThrow(routeId);
        Stop stop = stopRepository.findByIdAndRouteId(stopId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found with id " + stopId));

        if (!stop.getSequenceOrder().equals(req.getSequenceOrder()) &&
                stopRepository.existsByRouteIdAndSequenceOrder(routeId, req.getSequenceOrder())) {
            throw new DuplicateResourceException(
                    "A stop with sequence order " + req.getSequenceOrder() + " already exists on this route");
        }

        stop.setStopName(req.getStopName());
        stop.setSequenceOrder(req.getSequenceOrder());
        stop.setLatitude(req.getLatitude());
        stop.setLongitude(req.getLongitude());
        stop.setLandmark(req.getLandmark());
        return toResponse(stopRepository.save(stop));
    }

    public void deleteStop(String routeId, String stopId) {
        findRouterOrThrow(routeId);
        Stop stop = stopRepository.findByIdAndRouteId(stopId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Stop not found with id " + stopId));
        stopRepository.delete(stop);
    }

    @Transactional
    public List<StopResponse> reorderStops(String routeId, StopReorderRequest req) {
        findRouterOrThrow(routeId);
        for (StopReorderItem item : req.getStops()) {
            Stop stop = stopRepository.findByIdAndRouteId(item.getStopId(), routeId)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Stop not found with id " + item.getStopId() + " on route " + routeId));

            stop.setSequenceOrder(item.getSequenceOrder());
            stopRepository.save(stop);
        }
        return getAllStopsByRouteId(routeId);
    }
}
