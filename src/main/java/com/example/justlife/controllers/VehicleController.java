package com.example.justlife.controllers;

import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.services.VehicleService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {
    private final VehicleService vehicleService;

    @PostMapping
    ResponseDTO<VehicleDTO> createVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.createVehicle(vehicleDTO);
    }

    @PostMapping("/add/cleaners")
    ResponseDTO<VehicleDTO> assignCleanersToVehicle(@RequestBody VehicleDTO vehicleDTO) {
        return vehicleService.assignCleanersToVehicle(vehicleDTO);
    }
}
