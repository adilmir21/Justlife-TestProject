package com.example.justlife.controllers;

import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.services.CleaningProfessionalService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cleaning")
public class CleaningProfessionalController {
    private final CleaningProfessionalService cleaningProfessionalService;

    @PostMapping
    public ResponseDTO<CleaningProfessionalDTO> createCleaningProfessional(@RequestBody CleaningProfessionalDTO cleaningProfessionalDTO) {
        return cleaningProfessionalService.createCleaningProfessional(cleaningProfessionalDTO);
    }
    @GetMapping("/{id}")
    public ResponseDTO<CleaningProfessionalDTO> getCleaningProfessionals(@PathVariable Integer id) {
        return cleaningProfessionalService.getCleaningProfessional(id);
    }
    @PutMapping("/{id}")
    public ResponseDTO<CleaningProfessionalDTO> assignVehicle(@PathVariable Integer id, @RequestBody VehicleDTO vehicleDTO) {
        return cleaningProfessionalService.assignVehicleToCleaningProfessional(id,vehicleDTO);
    }
}
