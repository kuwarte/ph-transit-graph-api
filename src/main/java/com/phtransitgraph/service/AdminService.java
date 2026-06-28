package com.phtransitgraph.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.response.AnalyticsResponse;
import com.phtransitgraph.dto.response.PageResponse;
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

    public PageResponse<UserResponse> getAllUsers(int page, int size) {
        Page<User> result = userRepository.findAll(PageRequest.of(page, size));
        return new PageResponse<>(
                result.getContent().stream().map(this::toResponse).toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages(),
                result.isLast());
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
