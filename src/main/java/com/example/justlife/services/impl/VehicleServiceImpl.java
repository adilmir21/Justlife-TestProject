package com.example.justlife.services.impl;

import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.exceptions.InvalidArgumentsException;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.VehicleRepository;
import com.example.justlife.services.VehicleService;
import com.example.justlife.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {
    private final VehicleRepository vehicleRepository;
    private final CleaningProfessionalRepository cleaningProfessionalRepository;

    @Transactional
    @Override
    public ResponseDTO<VehicleDTO> createVehicle(VehicleDTO vehicleDTO) {
        log.info("VehicleServiceImpl :: createVehicle starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<VehicleDTO> responseDTO = new ResponseDTO<>();

        if (vehicleDTO.getCleaningProfessionals() != null && vehicleDTO.getCleaningProfessionals().size() > 5) {
            throw new InvalidArgumentsException("More than 5 cleaners can not be assigned to vehicles");
        }
        VehicleEntity vehicleEntity = new VehicleEntity();
        BeanUtils.copyProperties(vehicleDTO, vehicleEntity);

        List<CleaningProfessionalEntity> cleaningProfessionals = vehicleDTO.getCleaningProfessionals().stream()
                .map(id -> cleaningProfessionalRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cleaning Professional not found with ID: " + id)))
                .toList();
        vehicleEntity.setCleaningProfessionals(cleaningProfessionals);

        VehicleDTO createdVehicle = new VehicleDTO();
        BeanUtils.copyProperties(vehicleRepository.save(vehicleEntity), createdVehicle);
        responseDTO.setData(createdVehicle);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("VehicleServiceImpl :: createVehicle ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Override
    public ResponseDTO<VehicleDTO> assignCleanersToVehicle(VehicleDTO vehicleDTO) {
        log.info("VehicleServiceImpl :: assignCleanersToVehicle starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<VehicleDTO> responseDTO = new ResponseDTO<>();
        if (vehicleDTO != null && vehicleDTO.getCleaningProfessionals().size() > 5) {
            throw new InvalidArgumentsException("More than 5 cleaners can not be assigned to vehicles");
        }
        VehicleEntity vehicleEntity = vehicleRepository
                .findById(vehicleDTO.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found with ID: " + vehicleDTO.getId()));

        if (vehicleEntity.getCleaningProfessionals() != null && (vehicleEntity.getCleaningProfessionals().size() + vehicleDTO.getCleaningProfessionals().size()) > 5) {
            throw new InvalidArgumentsException("More than 5 cleaners can not be assigned to vehicles");
        }

        List<CleaningProfessionalEntity> cleaningProfessionals = vehicleDTO.getCleaningProfessionals().stream()
                .map(id -> cleaningProfessionalRepository
                        .findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Cleaning Professional not found with ID: " + id)))
                .toList();
        vehicleEntity.setCleaningProfessionals(cleaningProfessionals);
        vehicleRepository.save(vehicleEntity);
        responseDTO.setData(vehicleDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("VehicleServiceImpl :: assignCleanersToVehicle ends at {}ms", endTime - startTime);
        return responseDTO;
    }

}
