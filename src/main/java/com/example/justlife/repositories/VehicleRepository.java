package com.example.justlife.repositories;

import com.example.justlife.models.VehicleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VehicleRepository extends JpaRepository<VehicleEntity, Integer> {
    Optional<VehicleEntity> findVehicleEntityByVehicleNumber(String vehicleNumber);
}
