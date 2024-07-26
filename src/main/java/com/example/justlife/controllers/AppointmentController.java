package com.example.justlife.controllers;

import com.example.justlife.dtos.AppointmentDTO;
import com.example.justlife.dtos.CleaningProfessionalDTO;
import com.example.justlife.dtos.ResponseDTO;
import com.example.justlife.services.AppointmentService;
import com.example.justlife.utils.Constants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/appointments")
@RequiredArgsConstructor
@Slf4j
public class AppointmentController {
    private final AppointmentService appointmentService;

    @GetMapping("/availability")
    public ResponseDTO<List<CleaningProfessionalDTO>> checkAvailability(
            @RequestParam LocalDate date,
            @RequestParam(required = false) LocalDateTime startTime,
            @RequestParam(required = false) Integer duration) {

        ResponseDTO<List<CleaningProfessionalDTO>> responseDTO;
        if (startTime != null && duration != null) {
            responseDTO = appointmentService.checkAvailability(date, startTime, duration);
        } else {
            responseDTO = appointmentService.checkAvailability(date);
        }

        return responseDTO;
    }

    @PostMapping
    public ResponseDTO<AppointmentDTO> createBooking(@RequestBody AppointmentDTO appointmentDTO) {
        return appointmentService.createBooking(appointmentDTO);
    }

    @PutMapping("/{id}")
    public ResponseDTO<AppointmentDTO> updateBooking(@RequestBody AppointmentDTO appointmentDTO) {
       return appointmentService.updateBooking(appointmentDTO);
    }
}
