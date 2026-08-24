package com.smartpark.smartpark.repository;

import com.smartpark.smartpark.model.ParkingSpot;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ParkingSpotRepository extends JpaRepository<ParkingSpot, Long> {

    List<ParkingSpot> findByParkingLotId(Long parkingLotId);

}