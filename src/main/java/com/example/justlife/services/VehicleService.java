package com.example.justlife.services;

import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;

public interface VehicleService {
    ResponseDTO<VehicleDTO> createVehicle(VehicleDTO vehicleDTO);
    ResponseDTO<VehicleDTO> assignCleanersToVehicle(VehicleDTO vehicleDTO);
}
