package com.smartpark.smartpark.controller;

import com.smartpark.smartpark.model.User;
import com.smartpark.smartpark.service.UserService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Somente ADMIN consegue acessar por causa do SecurityConfig
    @GetMapping
    public List<User> getUsers() {
        return userService.getAllUsers();
    }

    // Usuário consulta o próprio perfil pelo JWT
    @GetMapping("/me")
    public User getMyProfile(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        return userService.getUserById(userId);
    }
}