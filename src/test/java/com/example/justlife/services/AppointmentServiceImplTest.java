package com.example.justlife.services;


import com.example.justlife.dtos.AppointmentDTO;
import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.exceptions.InvalidArgumentsException;
import com.example.justlife.exceptions.ResourceNotFoundException;
import com.example.justlife.models.AppointmentEntity;
import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.CustomerEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.AppointmentRepository;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.CustomerRepository;
import com.example.justlife.services.impl.AppointmentServiceImpl;
import com.example.justlife.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

public class AppointmentServiceImplTest {

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Mock
    private AppointmentRepository appointmentRepository;

    @Mock
    private CleaningProfessionalRepository cleaningProfessionalRepository;

    @Mock
    private CustomerRepository customerRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void checkAvailability_Success() {
        LocalDate date = LocalDate.now();

        // Set up the cleaning professional entity with initialized appointments list
        CleaningProfessionalEntity professional = new CleaningProfessionalEntity();
        professional.setId(1);
        professional.setAppointments(new ArrayList<>()); // Initialize the appointments list

        // Mock repository call
        when(cleaningProfessionalRepository.findAll()).thenReturn(List.of(professional));

        // Call the service method
        ResponseDTO<List<CleaningProfessionalDTO>> responseDTO = appointmentService.checkAvailability(date);

        // Assertions
        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().size());
        assertEquals(1, responseDTO.getData().get(0).getId());
    }


    @Test
    public void checkAvailability_WithTime_Success() {
        LocalDateTime start = LocalDateTime.now();
        Integer duration = 2;

        // Set up the cleaning professional entity
        CleaningProfessionalEntity professional = new CleaningProfessionalEntity();
        professional.setId(1);
        professional.setAppointments(new ArrayList<>()); // Initialize the appointments list

        // Mock repository call
        when(cleaningProfessionalRepository.findAll()).thenReturn(List.of(professional));

        // Call the service method
        ResponseDTO<List<CleaningProfessionalDTO>> responseDTO = appointmentService.checkAvailability(LocalDate.now(), start, duration);

        // Assertions
        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().size());
        assertEquals(1, responseDTO.getData().get(0).getId());
    }


    @Test
    public void createBooking_Success() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        while (startTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
            startTime = startTime.plusDays(1);
        }

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setCustomer(1);
        appointmentDTO.setCleaningProfessionals(List.of(1));
        appointmentDTO.setStartTime(startTime);
        appointmentDTO.setDuration(2);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        CleaningProfessionalEntity professionalEntity = new CleaningProfessionalEntity();
        professionalEntity.setId(1);
        professionalEntity.setVehicle(new VehicleEntity());

        List<AppointmentEntity> appointments = new ArrayList<>();
        AppointmentEntity existingAppointment = new AppointmentEntity();
        existingAppointment.setStartTime(startTime.minusDays(1).minusHours(1));
        existingAppointment.setEndTime(startTime.minusDays(1).plusHours(1));
        appointments.add(existingAppointment);

        professionalEntity.setAppointments(appointments);

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1);
        appointmentEntity.setStartTime(startTime);
        appointmentEntity.setEndTime(startTime.plusHours(2));
        appointmentEntity.setDuration(2);

        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customerEntity));
        when(cleaningProfessionalRepository.findById(anyInt())).thenReturn(Optional.of(professionalEntity));
        when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointmentEntity);

        ResponseDTO<AppointmentDTO> responseDTO = appointmentService.createBooking(appointmentDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getId());
    }




    @Test
    public void createBooking_InvalidArguments() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setDuration(2);

        assertThrows(InvalidArgumentsException.class, () -> appointmentService.createBooking(appointmentDTO));
    }

    @Test
    public void createBooking_InvalidStartTime() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setCustomer(1);
        appointmentDTO.setCleaningProfessionals(List.of(1));
        appointmentDTO.setStartTime(LocalDateTime.now().withHour(7));
        appointmentDTO.setDuration(2);

        assertThrows(InvalidArgumentsException.class, () -> appointmentService.createBooking(appointmentDTO));
    }

    @Test
    public void updateBooking_Success() {
        LocalDateTime startTime = LocalDateTime.now().plusHours(1);
        while (startTime.getDayOfWeek() == DayOfWeek.FRIDAY) {
            startTime = startTime.plusDays(1);
        }

        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1);  // Ensure the ID is set
        appointmentDTO.setCustomer(1);
        appointmentDTO.setCleaningProfessionals(List.of(1));
        appointmentDTO.setStartTime(startTime);
        appointmentDTO.setDuration(2);

        CustomerEntity customerEntity = new CustomerEntity();
        customerEntity.setId(1);

        CleaningProfessionalEntity professionalEntity = new CleaningProfessionalEntity();
        professionalEntity.setId(1);
        professionalEntity.setVehicle(new VehicleEntity());

        List<AppointmentEntity> appointments = new ArrayList<>();
        AppointmentEntity existingAppointment = new AppointmentEntity();
        existingAppointment.setStartTime(startTime.minusDays(1).minusHours(1));
        existingAppointment.setEndTime(startTime.minusDays(1).plusHours(1));
        existingAppointment.setId(1);
        appointments.add(existingAppointment);

        professionalEntity.setAppointments(appointments);

        AppointmentEntity appointmentEntity = new AppointmentEntity();
        appointmentEntity.setId(1);
        appointmentEntity.setStartTime(startTime);
        appointmentEntity.setEndTime(startTime.plusHours(2));
        appointmentEntity.setDuration(2);

        when(appointmentRepository.findById(1)).thenReturn(Optional.of(appointmentEntity));  // Match the ID
        when(customerRepository.findById(anyInt())).thenReturn(Optional.of(customerEntity));
        when(cleaningProfessionalRepository.findById(anyInt())).thenReturn(Optional.of(professionalEntity));
        when(appointmentRepository.save(any(AppointmentEntity.class))).thenReturn(appointmentEntity);

        ResponseDTO<AppointmentDTO> responseDTO = appointmentService.updateBooking(appointmentDTO);

        assertEquals(Constants.STATUS_SUCCESS, responseDTO.getStatusCode());
        assertNotNull(responseDTO.getData());
        assertEquals(1, responseDTO.getData().getId());
    }


    @Test
    public void updateBooking_NotFound() {
        AppointmentDTO appointmentDTO = new AppointmentDTO();
        appointmentDTO.setId(1);

        when(appointmentRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> appointmentService.updateBooking(appointmentDTO));
    }
}
