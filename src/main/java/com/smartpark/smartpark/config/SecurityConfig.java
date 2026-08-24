package com.smartpark.smartpark.config;

import com.nimbusds.jose.jwk.source.ImmutableSecret;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;

import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;

import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.nio.charset.StandardCharsets;

@Configuration
public class SecurityConfig {

    @Value("${JWT_SECRET}")
    private String jwtSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(
            HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

        // Login e cadastro públicos
        .requestMatchers("/api/auth/**").permitAll()

        // Permite que o Spring retorne erros corretamente
        .requestMatchers("/error").permitAll()

        // Perfil do próprio usuário
        .requestMatchers(
                org.springframework.http.HttpMethod.GET,
                "/api/users/me"
        ).authenticated()

                        // Lista de usuários: somente ADMIN
                        .requestMatchers(
                                org.springframework.http.HttpMethod.GET,
                                "/api/users"
                        ).hasRole("ADMIN")

                        // Criar estacionamento: somente ADMIN
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/parking-lots/**"
                        ).hasRole("ADMIN")

                        // Criar vagas: somente ADMIN
                        .requestMatchers(
                                org.springframework.http.HttpMethod.POST,
                                "/api/parking-spots/**"
                        ).hasRole("ADMIN")

                        // Alterar vagas: somente ADMIN
                        .requestMatchers(
                                org.springframework.http.HttpMethod.PATCH,
                                "/api/parking-spots/**"
                        ).hasRole("ADMIN")

                        // Todo o restante exige autenticação
                        .anyRequest().authenticated()
                )

                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwt ->
                                jwt.jwtAuthenticationConverter(
                                        jwtAuthenticationConverter()
                                )
                        )
                );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {

        JwtGrantedAuthoritiesConverter authoritiesConverter =
                new JwtGrantedAuthoritiesConverter();

        authoritiesConverter.setAuthoritiesClaimName("role");
        authoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter authenticationConverter =
                new JwtAuthenticationConverter();

        authenticationConverter.setJwtGrantedAuthoritiesConverter(
                authoritiesConverter
        );

        return authenticationConverter;
    }

    @Bean
    public JwtEncoder jwtEncoder() {

        SecretKey secretKey = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return new NimbusJwtEncoder(
                new ImmutableSecret<>(secretKey)
        );
    }

    @Bean
    public JwtDecoder jwtDecoder() {

        SecretKey secretKey = new SecretKeySpec(
                jwtSecret.getBytes(StandardCharsets.UTF_8),
                "HmacSHA256"
        );

        return NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }
}