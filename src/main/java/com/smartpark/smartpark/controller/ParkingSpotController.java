package com.smartpark.smartpark.controller;

import com.smartpark.smartpark.model.ParkingSpot;
import com.smartpark.smartpark.service.ParkingSpotService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/parking-spots")
public class ParkingSpotController {

    private final ParkingSpotService parkingSpotService;

    public ParkingSpotController(ParkingSpotService parkingSpotService) {
        this.parkingSpotService = parkingSpotService;
    }

    // Cria uma vaga em determinado estacionamento
    @PostMapping("/parking-lot/{parkingLotId}")
    public ParkingSpot createParkingSpot(
            @PathVariable Long parkingLotId,
            @RequestBody ParkingSpot parkingSpot) {

        return parkingSpotService.createParkingSpot(
                parkingLotId,
                parkingSpot
        );
    }

    // Lista todas as vagas de determinado estacionamento
    @GetMapping("/parking-lot/{parkingLotId}")
    public List<ParkingSpot> getSpotsByParkingLot(
            @PathVariable Long parkingLotId) {

        return parkingSpotService.getSpotsByParkingLot(parkingLotId);
    }

    // Lista todas as vagas cadastradas no sistema
    @GetMapping
    public List<ParkingSpot> getAllParkingSpots() {

        return parkingSpotService.getAllParkingSpots();
    }

    // Lista vagas disponíveis para determinado período
    @GetMapping("/parking-lot/{parkingLotId}/available")
    public List<ParkingSpot> getAvailableSpots(
            @PathVariable Long parkingLotId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime) {

        return parkingSpotService.getAvailableSpots(
                parkingLotId,
                startTime,
                endTime
        );
    }
}