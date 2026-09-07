package com.smartpark.smartpark.service;

import com.smartpark.smartpark.model.User;
import com.smartpark.smartpark.model.Vehicle;
import com.smartpark.smartpark.repository.UserRepository;
import com.smartpark.smartpark.repository.VehicleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class VehicleService {

    private final VehicleRepository vehicleRepository;
    private final UserRepository userRepository;

    public VehicleService(
            VehicleRepository vehicleRepository,
            UserRepository userRepository) {

        this.vehicleRepository = vehicleRepository;
        this.userRepository = userRepository;
    }

    public Vehicle createVehicle(Long userId, Vehicle vehicle) {

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Usuário não encontrado"
                        )
                );

        String plate = vehicle.getPlate().trim().toUpperCase();

        if (vehicleRepository.existsByPlateIgnoreCase(plate)) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta placa já está cadastrada"
            );
        }

        vehicle.setPlate(plate);
        vehicle.setUser(user);

        return vehicleRepository.save(vehicle);
    }

    public List<Vehicle> getAllVehicles() {
        return vehicleRepository.findAll();
    }

    public List<Vehicle> getVehiclesByUser(Long userId) {
        return vehicleRepository.findByUserId(userId);
    }
}