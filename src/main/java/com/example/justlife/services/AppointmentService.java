package com.example.justlife.services;

import com.example.justlife.dtos.AppointmentDTO;
import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface AppointmentService {
    ResponseDTO<List<CleaningProfessionalDTO>> checkAvailability(LocalDate date);
    ResponseDTO<List<CleaningProfessionalDTO>> checkAvailability(LocalDate date, LocalDateTime startTime, Integer duration);
    ResponseDTO<AppointmentDTO> createBooking(AppointmentDTO appointmentDTO);
    ResponseDTO<AppointmentDTO> updateBooking(AppointmentDTO appointmentDTO);
}
