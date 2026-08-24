package com.smartpark.smartpark.controller;

import com.smartpark.smartpark.model.Vehicle;
import com.smartpark.smartpark.service.VehicleService;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/vehicles")
public class VehicleController {

    private final VehicleService vehicleService;

    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @PostMapping("/me")
    public Vehicle createMyVehicle(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody Vehicle vehicle) {

        Long userId = Long.valueOf(jwt.getSubject());

        return vehicleService.createVehicle(userId, vehicle);
    }

    @GetMapping("/me")
    public List<Vehicle> getMyVehicles(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        return vehicleService.getVehiclesByUser(userId);
    }

    @GetMapping
    public List<Vehicle> getAllVehicles() {
        return vehicleService.getAllVehicles();
    }
}