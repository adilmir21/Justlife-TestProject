package com.example.justlife.services.impl;

import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.AppointmentEntity;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.VehicleRepository;
import com.example.justlife.services.CleaningProfessionalService;
import com.example.justlife.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CleaningProfessionalServiceImpl implements CleaningProfessionalService {

    private final CleaningProfessionalRepository cleaningProfessionalRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    @Transactional
    public ResponseDTO<CleaningProfessionalDTO> createCleaningProfessional(CleaningProfessionalDTO cleaningProfessionalDTO) {
        log.info("CleaningProfessionalServiceImpl :: createCleaningProfessional starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<CleaningProfessionalDTO> responseDTO = new ResponseDTO<>();
        CleaningProfessionalEntity cleaningProfessional = new CleaningProfessionalEntity();
        if (cleaningProfessionalDTO.getVehicle() != null) {
            VehicleEntity vehicleEntity = vehicleRepository
                    .findById(cleaningProfessionalDTO.getVehicle())
                    .orElseThrow(() -> new ResourceNotFoundException("No Vehicle against ID found "));

            cleaningProfessional.setVehicle(vehicleEntity);
        }
        BeanUtils.copyProperties(cleaningProfessionalDTO, cleaningProfessional);
        CleaningProfessionalDTO createdCleaningProfessional = new CleaningProfessionalDTO();
        BeanUtils.copyProperties(cleaningProfessionalRepository.save(cleaningProfessional), createdCleaningProfessional);
        createdCleaningProfessional.setVehicle(cleaningProfessionalDTO.getVehicle());
        responseDTO.setData(createdCleaningProfessional);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("CleaningProfessionalServiceImpl :: createCleaningProfessional ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Override
    public ResponseDTO<CleaningProfessionalDTO> getCleaningProfessional(Integer id) {
        log.info("CleaningProfessionalServiceImpl :: getCleaningProfessional starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<CleaningProfessionalDTO> responseDTO = new ResponseDTO<>();
        CleaningProfessionalEntity cleaningProfessional = cleaningProfessionalRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("No CleaningProfessional found with id: " + id));
        CleaningProfessionalDTO cleaningProfessionalDTO = new CleaningProfessionalDTO();
        BeanUtils.copyProperties(cleaningProfessional, cleaningProfessionalDTO);
        cleaningProfessionalDTO.setVehicle(cleaningProfessional.getVehicle() != null ? cleaningProfessional
                .getVehicle().getId() : null);
        cleaningProfessionalDTO.setAppointments(cleaningProfessional.getAppointments().stream()
                .map(AppointmentEntity::getId)
                .toList()
        );
        responseDTO.setData(cleaningProfessionalDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("CleaningProfessionalServiceImpl :: getCleaningProfessional ends at {}ms", endTime - startTime);
        return responseDTO;
    }

    @Override
    @Transactional
    public ResponseDTO<CleaningProfessionalDTO> assignVehicleToCleaningProfessional(Integer professionalId, VehicleDTO vehicleDTO) {
        log.info("CleaningProfessionalServiceImpl :: assignVehicleToCleaningProfessional starts");
        Long startTime = System.currentTimeMillis();
        ResponseDTO<CleaningProfessionalDTO> responseDTO = new ResponseDTO<>();
        CleaningProfessionalEntity cleaningProfessional = cleaningProfessionalRepository.findById(professionalId)
                .orElseThrow(()-> new ResourceNotFoundException("No CleaningProfessional found with id: " + professionalId));
        VehicleEntity vehicle = vehicleRepository.findVehicleEntityByVehicleNumber(vehicleDTO.getVehicleNumber())
                        .orElseThrow(()-> new ResourceNotFoundException("No Vehicle found with number: " + vehicleDTO.getVehicleNumber()));
        cleaningProfessional.setVehicle(vehicle);
        CleaningProfessionalDTO cleaningProfessionalDTO = new CleaningProfessionalDTO();
        BeanUtils.copyProperties(cleaningProfessionalRepository.save(cleaningProfessional), cleaningProfessionalDTO);
        cleaningProfessionalDTO.setVehicle(vehicle.getId());
        cleaningProfessionalDTO.setAppointments(cleaningProfessional.getAppointments().stream()
                .map(AppointmentEntity::getId)
                .toList()
        );
        responseDTO.setData(cleaningProfessionalDTO);
        responseDTO.setStatusCode(Constants.STATUS_SUCCESS);
        Long endTime = System.currentTimeMillis();
        log.info("CleaningProfessionalServiceImpl :: assignVehicleToCleaningProfessional ends at {}ms", endTime - startTime);
        return responseDTO;
    }


}
