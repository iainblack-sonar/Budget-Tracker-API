package com.budgetly.api.controller;

import com.budgetly.api.dto.AuthResponse;
import com.budgetly.api.dto.LoginRequest;
import com.budgetly.api.dto.RegisterRequest;
import com.budgetly.auth.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        String token =
                authenticationService.register(
                        request.email(),
                        request.password(),
                        request.firstName(),
                        request.lastName());
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        String token = authenticationService.login(request.email(), request.password());
        return ResponseEntity.ok(new AuthResponse(token));
    }
}
