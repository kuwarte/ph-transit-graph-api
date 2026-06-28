package com.phtransitgraph.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.phtransitgraph.entity.Operator;
import com.phtransitgraph.entity.User;
import com.phtransitgraph.enums.Role;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.phtransitgraph.dto.request.LoginRequest;
import com.phtransitgraph.dto.request.OperatorRegisterRequest;
import com.phtransitgraph.dto.request.RegisterRequest;
import com.phtransitgraph.dto.response.AuthResponse;
import com.phtransitgraph.dto.response.UserResponse;
import com.phtransitgraph.exception.DuplicateResourceException;
import com.phtransitgraph.exception.ResourceNotFoundException;
import com.phtransitgraph.repository.OperatorRepository;
import com.phtransitgraph.repository.UserRepository;
import com.phtransitgraph.security.JwtUtil;

import jakarta.transaction.Transactional;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository, OperatorRepository operatorRepository,
            PasswordEncoder passwordEncoder, JwtUtil jwtUtil, AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException(
                    "Email '" + req.getEmail() + "' is already in use");
        }
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.COMMUTER)
                .build();

        user = userRepository.save(user);
        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());
        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }

    @Transactional
    public AuthResponse registerOperator(OperatorRegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new DuplicateResourceException(
                    "Email '" + req.getEmail() + "' is already in use");
        }

        if (req.getFranchiseNo() != null &&
                operatorRepository.existsByFranchiseNo(req.getFranchiseNo())) {
            throw new DuplicateResourceException(
                    "Franchise number '" + req.getFranchiseNo() + "' is already in use");

        }
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .role(Role.OPERATOR)
                .build();

        user = userRepository.save(user);

        Operator operator = Operator.builder()
                .user(user)
                .operatorName(req.getOperatorName())
                .franchiseNo(req.getFranchiseNo())
                .contactNumber(req.getContactNumber())
                .build();

        operatorRepository.save(operator);

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }

    public AuthResponse login(LoginRequest req) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));

        User user = userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getRole().name());

        return new AuthResponse(token, user.getId(), user.getEmail(), user.getFullName(), user.getRole().name());
    }

    public UserResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return new UserResponse(user.getId(), user.getEmail(), user.getFullName(), user.getRole().name(),
                user.isActive(), user.getCreatedAt(), user.getUpdatedAt());
    }
}
