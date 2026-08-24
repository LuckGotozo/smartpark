package com.smartpark.smartpark.service;

import com.smartpark.smartpark.model.ParkingSpot;
import com.smartpark.smartpark.model.Reservation;
import com.smartpark.smartpark.model.ReservationStatus;
import com.smartpark.smartpark.model.Vehicle;

import com.smartpark.smartpark.repository.ParkingSpotRepository;
import com.smartpark.smartpark.repository.ReservationRepository;
import com.smartpark.smartpark.repository.VehicleRepository;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final VehicleRepository vehicleRepository;
    private final ParkingSpotRepository parkingSpotRepository;

    public ReservationService(
            ReservationRepository reservationRepository,
            VehicleRepository vehicleRepository,
            ParkingSpotRepository parkingSpotRepository) {

        this.reservationRepository = reservationRepository;
        this.vehicleRepository = vehicleRepository;
        this.parkingSpotRepository = parkingSpotRepository;
    }

    public Reservation createReservation(
            Long userId,
            Long vehicleId,
            Long parkingSpotId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O horário final deve ser posterior ao horário inicial"
            );
        }

        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Veículo não encontrado"
                        )
                );

        if (!vehicle.getUser().getId().equals(userId)) {
            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode utilizar o veículo de outro usuário"
            );
        }

        ParkingSpot parkingSpot = parkingSpotRepository.findById(parkingSpotId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Vaga não encontrada"
                        )
                );

        if (!parkingSpot.isActive()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta vaga está desativada"
            );
        }

        List<Reservation> conflicts =
                reservationRepository
                        .findByParkingSpotIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                                parkingSpotId,
                                ReservationStatus.ACTIVE,
                                endTime,
                                startTime
                        );

        if (!conflicts.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT,
                    "Esta vaga já está reservada nesse horário"
            );
        }

        Reservation reservation = new Reservation();

        reservation.setVehicle(vehicle);
        reservation.setParkingSpot(parkingSpot);
        reservation.setStartTime(startTime);
        reservation.setEndTime(endTime);
        reservation.setStatus(ReservationStatus.ACTIVE);

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAllReservations() {

        List<Reservation> reservations =
                reservationRepository.findAll();

        updateCompletedReservations(reservations);

        return reservations;
    }

    public Reservation cancelReservation(
            Long userId,
            Long reservationId) {

        Reservation reservation =
                reservationRepository.findById(reservationId)
                        .orElseThrow(() ->
                                new ResponseStatusException(
                                        HttpStatus.NOT_FOUND,
                                        "Reserva não encontrada"
                                )
                        );

        if (!reservation.getVehicle()
                .getUser()
                .getId()
                .equals(userId)) {

            throw new ResponseStatusException(
                    HttpStatus.FORBIDDEN,
                    "Você não pode cancelar a reserva de outro usuário"
            );
        }

        if (reservation.getStatus() == ReservationStatus.CANCELLED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Esta reserva já está cancelada"
            );
        }

        if (reservation.getStatus() == ReservationStatus.COMPLETED) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Uma reserva concluída não pode ser cancelada"
            );
        }

        reservation.setStatus(ReservationStatus.CANCELLED);

        return reservationRepository.save(reservation);
    }

    private void updateCompletedReservations(
            List<Reservation> reservations) {

        LocalDateTime now = LocalDateTime.now();

        for (Reservation reservation : reservations) {

            if (reservation.getStatus() == ReservationStatus.ACTIVE
                    && reservation.getEndTime().isBefore(now)) {

                reservation.setStatus(
                        ReservationStatus.COMPLETED
                );

                reservationRepository.save(reservation);
            }
        }
    }
    public List<Reservation> getReservationsByUser(Long userId) {

    List<Reservation> reservations =
            reservationRepository.findByVehicleUserId(userId);

    updateCompletedReservations(reservations);

    return reservations;
}
}