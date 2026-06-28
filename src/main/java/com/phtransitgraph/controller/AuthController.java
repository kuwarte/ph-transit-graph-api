package com.phtransitgraph.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import com.phtransitgraph.dto.request.LoginRequest;
import com.phtransitgraph.dto.request.OperatorRegisterRequest;
import com.phtransitgraph.dto.request.RegisterRequest;
import com.phtransitgraph.dto.response.AuthResponse;
import com.phtransitgraph.dto.response.UserResponse;
import com.phtransitgraph.service.AuthService;
import jakarta.validation.Valid;

@Tag(name = "Auth", description = "Register, login, and get current user")
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @Operation(summary = "Register a new commuter account")
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(req));
    }

    @Operation(summary = "Register as a transport operator (pending admin verification)")
    @PostMapping("/register/operator")
    public ResponseEntity<AuthResponse> registerOperator(
            @Valid @RequestBody OperatorRegisterRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authService.registerOperator(req));
    }

    @Operation(summary = "Login and receive a JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    @Operation(summary = "Get the currently authenticated user")
    @GetMapping("/me")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(authService.getCurrentUser(userDetails.getUsername()));
    }
}
