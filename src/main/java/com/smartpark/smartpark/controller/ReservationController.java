package com.smartpark.smartpark.controller;

import com.smartpark.smartpark.model.Reservation;
import com.smartpark.smartpark.service.ReservationService;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    public ReservationController(
            ReservationService reservationService) {

        this.reservationService = reservationService;
    }

    @PostMapping
    public Reservation createReservation(
            @AuthenticationPrincipal Jwt jwt,
            @RequestParam Long vehicleId,
            @RequestParam Long parkingSpotId,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime startTime,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime endTime) {

        Long userId = Long.valueOf(jwt.getSubject());

        return reservationService.createReservation(
                userId,
                vehicleId,
                parkingSpotId,
                startTime,
                endTime
        );
    }


    @GetMapping("/me")
    public List<Reservation> getMyReservations(
            @AuthenticationPrincipal Jwt jwt) {

        Long userId = Long.valueOf(jwt.getSubject());

        return reservationService.getReservationsByUser(userId);
    }

    @PatchMapping("/{reservationId}/cancel")
    public Reservation cancelReservation(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable Long reservationId) {

        Long userId = Long.valueOf(jwt.getSubject());

        return reservationService.cancelReservation(
                userId,
                reservationId
        );
    }
}