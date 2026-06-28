package com.phtransitgraph.service;

import java.util.List;
import org.springframework.stereotype.Service;
import com.phtransitgraph.dto.request.FareRequest;
import com.phtransitgraph.dto.response.FareResponse;
import com.phtransitgraph.entity.Fare;
import com.phtransitgraph.entity.Route;
import com.phtransitgraph.entity.Stop;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.FareRepository;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.repository.StopRepository;
import com.phtransitgraph.security.OwnershipValidator;

@Service
public class FareService {

    private final FareRepository fareRepository;
    private final RouteRepository routeRepository;
    private final StopRepository stopRepository;
    private final OwnershipValidator ownershipValidator;

    public FareService(FareRepository fareRepository, RouteRepository routeRepository,
            StopRepository stopRepository, OwnershipValidator ownershipValidator) {
        this.fareRepository = fareRepository;
        this.routeRepository = routeRepository;
        this.stopRepository = stopRepository;
        this.ownershipValidator = ownershipValidator;

    }

    private FareResponse toResponse(Fare fare) {
        return new FareResponse(
                fare.getId(),
                fare.getRoute().getId(),
                fare.getFromStop().getId(),
                fare.getFromStop().getStopName(),
                fare.getToStop().getId(),
                fare.getToStop().getStopName(),
                fare.getBaseFare(),
                fare.getDiscountedFare(),
                fare.getEffectiveDate(),
                fare.getCreatedAt());
    }

    private Route findRouteOrThrow(String routeId) {
        return routeRepository.findById(routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Route not found with id " + routeId));
    }

    private Stop findStopOnRouteOrThrow(String stopId, String routeId, String label) {
        return stopRepository.findByIdAndRouteId(stopId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        label + " stop not found with id " + stopId + " on route " + routeId));
    }

    public List<FareResponse> getFaresByRouteId(String routeId) {
        findRouteOrThrow(routeId);
        return fareRepository.findByRouteId(routeId)
                .stream()
                .sorted((a, b) -> a.getFromStop().getSequenceOrder()
                        .compareTo(b.getFromStop().getSequenceOrder()))
                .map(this::toResponse)
                .toList();
    }

    public FareResponse calculateFare(String fromStopId, String toStopId) {
        return fareRepository
                .findTopByFromStopIdAndToStopIdOrderByEffectiveDateDesc(fromStopId, toStopId)
                .map(this::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "No fare found between stop " + fromStopId + " and stop " + toStopId));
    }

    public FareResponse addFare(String routeId, FareRequest req, String email) {
        Route route = findRouteOrThrow(routeId);

        ownershipValidator.assertOwnsRoute(route, email);

        if (req.getFromStopId().equals(req.getToStopId())) {
            throw new IllegalArgumentException("from stop and to stop cannot be the same");
        }

        Stop fromStop = findStopOnRouteOrThrow(req.getFromStopId(), routeId, "From");
        Stop toStop = findStopOnRouteOrThrow(req.getToStopId(), routeId, "To");

        if (fareRepository.existsByRouteIdAndFromStopIdAndToStopIdAndEffectiveDate(
                routeId, req.getFromStopId(), req.getToStopId(), req.getEffectiveDate())) {
            throw new DuplicateResourceException(
                    "A fare for this stop pair already exists on " + req.getEffectiveDate());
        }

        Fare fare = Fare.builder()
                .route(route)
                .fromStop(fromStop)
                .toStop(toStop)
                .baseFare(req.getBaseFare())
                .discountedFare(req.getDiscountedFare())
                .effectiveDate(req.getEffectiveDate())
                .build();

        return toResponse(fareRepository.save(fare));
    }

    public FareResponse updateFare(String routeId, String fareId, FareRequest req, String email) {
        Route route = findRouteOrThrow(routeId);

        ownershipValidator.assertOwnsRoute(route, email);

        Fare fare = fareRepository.findByIdAndRouteId(fareId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fare not found with id " + fareId));

        if (req.getFromStopId().equals(req.getToStopId())) {
            throw new IllegalArgumentException("from stop and to stop cannot be the same");
        }

        Stop fromStop = findStopOnRouteOrThrow(req.getFromStopId(), routeId, "From");
        Stop toStop = findStopOnRouteOrThrow(req.getToStopId(), routeId, "To");

        boolean stopPairChanged = !fare.getFromStop().getId().equals(req.getFromStopId())
                || !fare.getToStop().getId().equals(req.getToStopId());
        boolean dateChanged = !fare.getEffectiveDate().equals(req.getEffectiveDate());

        if ((stopPairChanged || dateChanged) &&
                fareRepository.existsByRouteIdAndFromStopIdAndToStopIdAndEffectiveDate(
                        routeId, req.getFromStopId(), req.getToStopId(), req.getEffectiveDate())) {
            throw new DuplicateResourceException(
                    "A fare for this stop pair already exists on " + req.getEffectiveDate());
        }

        fare.setFromStop(fromStop);
        fare.setToStop(toStop);
        fare.setBaseFare(req.getBaseFare());
        fare.setDiscountedFare(req.getDiscountedFare());
        fare.setEffectiveDate(req.getEffectiveDate());

        return toResponse(fareRepository.save(fare));
    }

    public void deleteFare(String routeId, String fareId, String email) {
        Route route = findRouteOrThrow(routeId);
        ownershipValidator.assertOwnsRoute(route, email);

        Fare fare = fareRepository.findByIdAndRouteId(fareId, routeId)
                .orElseThrow(() -> new ResourceNotFoundException("Fare not found with id " + fareId));
        fareRepository.delete(fare);
    }
}
