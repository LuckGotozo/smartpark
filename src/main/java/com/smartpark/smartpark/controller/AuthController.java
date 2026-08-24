package com.smartpark.smartpark.controller;

import com.smartpark.smartpark.dto.LoginRequest;
import com.smartpark.smartpark.dto.LoginResponse;
import com.smartpark.smartpark.service.AuthService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        return authService.login(request);
    }
}