package com.smartpark.smartpark.repository;

import com.smartpark.smartpark.model.Reservation;
import com.smartpark.smartpark.model.ReservationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByParkingSpotIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            Long parkingSpotId,
            ReservationStatus status,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    List<Reservation> findByParkingSpotParkingLotIdAndStatusAndStartTimeLessThanAndEndTimeGreaterThan(
            Long parkingLotId,
            ReservationStatus status,
            LocalDateTime endTime,
            LocalDateTime startTime
    );

    List<Reservation> findByVehicleUserId(Long userId);
}