package com.example.justlife.services;


import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.exceptions.InvalidArgumentsException;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.VehicleRepository;
import com.example.justlife.services.impl.VehicleServiceImpl;
import com.example.justlife.utils.Constants;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VehicleServiceImplTest {

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    @Mock
    private VehicleRepository vehicleRepository;

    @Mock
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    public VehicleServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createVehicle_Success() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setCleaningProfessionals(List.of(1, 2));

        CleaningProfessionalEntity professional1 = new CleaningProfessionalEntity();
        CleaningProfessionalEntity professional2 = new CleaningProfessionalEntity();

        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.of(professional1));
        when(cleaningProfessionalRepository.findById(2)).thenReturn(Optional.of(professional2));

        VehicleEntity vehicleEntity = new VehicleEntity();
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

        ResponseDTO<VehicleDTO> responseDTO = vehicleService.createVehicle(vehicleDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
    }

    @Test
    public void createVehicle_InvalidArgumentsException() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setCleaningProfessionals(List.of(1, 2, 3, 4, 5, 6));

        assertThrows(InvalidArgumentsException.class, () -> vehicleService.createVehicle(vehicleDTO));
    }

    @Test
    public void assignCleanersToVehicle_Success() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1);
        vehicleDTO.setCleaningProfessionals(List.of(1, 2));

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setCleaningProfessionals(List.of());

        CleaningProfessionalEntity professional1 = new CleaningProfessionalEntity();
        CleaningProfessionalEntity professional2 = new CleaningProfessionalEntity();

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicleEntity));
        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.of(professional1));
        when(cleaningProfessionalRepository.findById(2)).thenReturn(Optional.of(professional2));
        when(vehicleRepository.save(any(VehicleEntity.class))).thenReturn(vehicleEntity);

        ResponseDTO<VehicleDTO> responseDTO = vehicleService.assignCleanersToVehicle(vehicleDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        verify(vehicleRepository, times(1)).save(vehicleEntity);
    }

    @Test
    public void assignCleanersToVehicle_ExceedsMaxCleaners() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1);
        vehicleDTO.setCleaningProfessionals(List.of(1, 2, 3, 4, 5, 6));

        assertThrows(InvalidArgumentsException.class, () -> vehicleService.assignCleanersToVehicle(vehicleDTO));
    }

    @Test
    public void assignCleanersToVehicle_VehicleNotFound() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1);
        vehicleDTO.setCleaningProfessionals(List.of(1));

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.assignCleanersToVehicle(vehicleDTO));
    }

    @Test
    public void assignCleanersToVehicle_CleaningProfessionalNotFound() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setId(1);
        vehicleDTO.setCleaningProfessionals(List.of(1));

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setCleaningProfessionals(List.of());

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicleEntity));
        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> vehicleService.assignCleanersToVehicle(vehicleDTO));
    }
}
