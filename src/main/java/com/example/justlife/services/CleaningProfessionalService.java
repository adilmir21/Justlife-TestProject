package com.example.justlife.services;


import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;

public interface CleaningProfessionalService {
    ResponseDTO<CleaningProfessionalDTO> createCleaningProfessional(CleaningProfessionalDTO cleaningProfessionalDTO);
    ResponseDTO<CleaningProfessionalDTO> getCleaningProfessional(Integer id);
    ResponseDTO<CleaningProfessionalDTO> assignVehicleToCleaningProfessional(Integer professionalId, VehicleDTO vehicleDTO);
}
