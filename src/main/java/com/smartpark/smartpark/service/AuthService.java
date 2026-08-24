package com.smartpark.smartpark.service;

import com.smartpark.smartpark.dto.LoginRequest;
import com.smartpark.smartpark.dto.LoginResponse;
import com.smartpark.smartpark.model.User;
import com.smartpark.smartpark.repository.UserRepository;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.smartpark.smartpark.dto.RegisterRequest;
import com.smartpark.smartpark.model.Role;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.UNAUTHORIZED,
                                "E-mail ou senha inválidos"
                        )
                );

        boolean passwordMatches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!passwordMatches) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "E-mail ou senha inválidos"
            );
        }

        String token = jwtService.generateToken(user);

        return new LoginResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                token
        );
    }
    public User register(RegisterRequest request) {

    if (userRepository.findByEmail(request.getEmail()).isPresent()) {
        throw new ResponseStatusException(
                HttpStatus.CONFLICT,
                "Este e-mail já está cadastrado"
        );
    }

    User user = new User();

    user.setName(request.getName());
    user.setEmail(request.getEmail());
    user.setPassword(
            passwordEncoder.encode(request.getPassword())
    );

    // Cadastro público SEMPRE cria usuário comum
    user.setRole(Role.USER);

    return userRepository.save(user);
}
}