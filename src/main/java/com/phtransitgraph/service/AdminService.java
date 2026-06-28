package com.phtransitgraph.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.response.AnalyticsResponse;
import com.phtransitgraph.dto.response.UserResponse;
import com.phtransitgraph.entity.User;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.OperatorRepository;
import com.phtransitgraph.repository.ReportRepository;
import com.phtransitgraph.repository.RouteRepository;
import com.phtransitgraph.repository.UserRepository;

@Service
public class AdminService {
    private final UserRepository userRepository;
    private final OperatorRepository operatorRepository;
    private final RouteRepository routeRepository;
    private final ReportRepository reportRepository;

    public AdminService(UserRepository userRepository, OperatorRepository operatorRepository,
            RouteRepository routeRepository, ReportRepository reportRepository) {
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
        this.routeRepository = routeRepository;
        this.reportRepository = reportRepository;
    }

    private UserResponse toResponse(User user) {
        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole().name(),
                user.isActive(),
                user.getCreatedAt(), user.getUpdatedAt());
    }

    public List<UserResponse> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public UserResponse getUserById(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        return toResponse(user);
    }

    public UserResponse deactivateUser(String id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "User not found with id: " + id));
        user.setActive(false);
        return toResponse(userRepository.save(user));
    }

    public AnalyticsResponse getAnalytics() {
        return new AnalyticsResponse(
                userRepository.count(),
                operatorRepository.count(),
                routeRepository.count(),
                reportRepository.count());
    }

}
