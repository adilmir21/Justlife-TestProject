package com.example.justlife.dtos;

import com.example.justlife.models.AppointmentEntity;
import com.example.justlife.models.VehicleEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CleaningProfessionalDTO {
    private Integer id;
    private String name;
    private Integer vehicle;
    private List<Integer> appointments;
    private LocalTime workStartTime;
    private LocalTime workEndTime;
}
