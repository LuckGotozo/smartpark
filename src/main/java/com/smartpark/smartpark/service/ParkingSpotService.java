package com.smartpark.smartpark.service;

import com.smartpark.smartpark.model.ParkingLot;
import com.smartpark.smartpark.model.ParkingSpot;
import com.smartpark.smartpark.model.Reservation;
import com.smartpark.smartpark.model.ReservationStatus;
import com.smartpark.smartpark.repository.ParkingLotRepository;
import com.smartpark.smartpark.repository.ParkingSpotRepository;
import com.smartpark.smartpark.repository.ReservationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ParkingSpotService {

    private final ParkingSpotRepository parkingSpotRepository;
    private final ParkingLotRepository parkingLotRepository;
    private final ReservationRepository reservationRepository;

    public ParkingSpotService(
            ParkingSpotRepository parkingSpotRepository,
            ParkingLotRepository parkingLotRepository,
            ReservationRepository reservationRepository) {

        this.parkingSpotRepository = parkingSpotRepository;
        this.parkingLotRepository = parkingLotRepository;
        this.reservationRepository = reservationRepository;
    }

    // Cria uma nova vaga dentro de um estacionamento
    public ParkingSpot createParkingSpot(
            Long parkingLotId,
            ParkingSpot parkingSpot) {

        ParkingLot parkingLot = parkingLotRepository.findById(parkingLotId)
                .orElseThrow(() ->
                        new ResponseStatusException(
                                HttpStatus.NOT_FOUND,
                                "Estacionamento não encontrado"
                        )
                );

        parkingSpot.setParkingLot(parkingLot);

        return parkingSpotRepository.save(parkingSpot);
    }

    // Lista as vagas de um estacionamento
    public List<ParkingSpot> getSpotsByParkingLot(Long parkingLotId) {

        return parkingSpotRepository.findByParkingLotId(parkingLotId);
    }

    // Lista todas as vagas cadastradas
    public List<ParkingSpot> getAllParkingSpots() {

        return parkingSpotRepository.findAll();
    }

    // Lista somente as vagas disponíveis em determinado período
    public List<ParkingSpot> getAvailableSpots(
            Long parkingLotId,
            LocalDateTime startTime,
            LocalDateTime endTime) {

        // Horário inválido
        if (!endTime.isAfter(startTime)) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "O horário final deve ser posterior ao horário inicial"
            );
        }

        // Verifica se o estacionamento existe
        if (!parkingLotRepository.existsById(parkingLotId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Estacionamento não encontrado"
            );
        }

        // Busca todas as vagas do estacionamento
        List<ParkingSpot> allSpots =
                parkingSpotRepository.findByParkingLotId(parkingLotId);

        // Busca reservas que entram em conflito com o período informado
        List<Reservation> conflictingReservations =
        reservationRepository
                .findByParkingSpotParkingLotIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
                        parkingLotId,
                        ReservationStatus.ACTIVE,
                        endTime,
                        startTime
                );

        // Pega o ID das vagas que estão ocupadas
        Set<Long> occupiedSpotIds = conflictingReservations.stream()
                .map(reservation -> reservation.getParkingSpot().getId())
                .collect(Collectors.toSet());

        // Retorna apenas vagas ativas e que não estejam ocupadas
        return allSpots.stream()
        .filter(spot -> spot != null && spot.isActive())
        .filter(spot -> !occupiedSpotIds.contains(spot.getId()))
        .toList();
    }
}