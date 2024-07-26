package com.example.justlife.services;

import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.dtos.VehicleDTO;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.AppointmentEntity;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.CustomerEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.VehicleRepository;
import com.example.justlife.services.impl.CleaningProfessionalServiceImpl;
import com.example.justlife.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CleaningProfessionalServiceImplTest {

    @InjectMocks
    private CleaningProfessionalServiceImpl cleaningProfessionalService;

    @Mock
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    @Mock
    private VehicleRepository vehicleRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void createCleaningProfessional_Success() {
        CleaningProfessionalDTO cleaningProfessionalDTO = new CleaningProfessionalDTO();
        cleaningProfessionalDTO.setVehicle(1);

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setId(1);

        CleaningProfessionalEntity cleaningProfessionalEntity = new CleaningProfessionalEntity();
        cleaningProfessionalEntity.setVehicle(vehicleEntity);

        when(vehicleRepository.findById(1)).thenReturn(Optional.of(vehicleEntity));
        when(cleaningProfessionalRepository.save(any(CleaningProfessionalEntity.class))).thenReturn(cleaningProfessionalEntity);

        ResponseDTO<CleaningProfessionalDTO> responseDTO = cleaningProfessionalService.createCleaningProfessional(cleaningProfessionalDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getVehicle());
        verify(cleaningProfessionalRepository, times(1)).save(any(CleaningProfessionalEntity.class));
    }

    @Test
    public void createCleaningProfessional_VehicleNotFound() {
        CleaningProfessionalDTO cleaningProfessionalDTO = new CleaningProfessionalDTO();
        cleaningProfessionalDTO.setVehicle(1);

        when(vehicleRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cleaningProfessionalService.createCleaningProfessional(cleaningProfessionalDTO));
    }

    @Test
    public void getCleaningProfessional_Success() {
        CleaningProfessionalEntity cleaningProfessionalEntity = new CleaningProfessionalEntity();
        cleaningProfessionalEntity.setId(1);
        cleaningProfessionalEntity.setVehicle(new VehicleEntity());
        cleaningProfessionalEntity.setAppointments(List.of(new AppointmentEntity()));

        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.of(cleaningProfessionalEntity));

        ResponseDTO<CleaningProfessionalDTO> responseDTO = cleaningProfessionalService.getCleaningProfessional(1);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getAppointments().size());
        verify(cleaningProfessionalRepository, times(1)).findById(1);
    }

    @Test
    public void getCleaningProfessional_NotFound() {
        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cleaningProfessionalService.getCleaningProfessional(1));
    }

    @Test
    public void assignVehicleToCleaningProfessional_Success() {

        AppointmentEntity appointment = new AppointmentEntity();
        appointment.setId(1);
        appointment.setCustomer(new CustomerEntity(1,"test", "test@gmail.com", "123455",null));
        CleaningProfessionalEntity cleaningProfessionalEntity = new CleaningProfessionalEntity();
        cleaningProfessionalEntity.setId(1);
        cleaningProfessionalEntity.setAppointments(List.of(appointment));

        VehicleEntity vehicleEntity = new VehicleEntity();
        vehicleEntity.setId(1);
        vehicleEntity.setVehicleNumber("ABC123");

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleNumber("ABC123");

        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.of(cleaningProfessionalEntity));
        when(vehicleRepository.findVehicleEntityByVehicleNumber("ABC123")).thenReturn(Optional.of(vehicleEntity));
        when(cleaningProfessionalRepository.save(any(CleaningProfessionalEntity.class))).thenReturn(cleaningProfessionalEntity);

        ResponseDTO<CleaningProfessionalDTO> responseDTO = cleaningProfessionalService.assignVehicleToCleaningProfessional(1, vehicleDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getVehicle());
        verify(cleaningProfessionalRepository, times(1)).save(any(CleaningProfessionalEntity.class));
    }

    @Test
    public void assignVehicleToCleaningProfessional_CleaningProfessionalNotFound() {
        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleNumber("ABC123");

        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cleaningProfessionalService.assignVehicleToCleaningProfessional(1, vehicleDTO));
    }

    @Test
    public void assignVehicleToCleaningProfessional_VehicleNotFound() {
        CleaningProfessionalEntity cleaningProfessionalEntity = new CleaningProfessionalEntity();
        cleaningProfessionalEntity.setId(1);

        VehicleDTO vehicleDTO = new VehicleDTO();
        vehicleDTO.setVehicleNumber("ABC123");

        when(cleaningProfessionalRepository.findById(1)).thenReturn(Optional.of(cleaningProfessionalEntity));
        when(vehicleRepository.findVehicleEntityByVehicleNumber("ABC123")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> cleaningProfessionalService.assignVehicleToCleaningProfessional(1, vehicleDTO));
    }
}

