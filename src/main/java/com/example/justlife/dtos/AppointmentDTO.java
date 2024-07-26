package com.example.justlife.dtos;

import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.CustomerEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class AppointmentDTO {
    private Integer id;
    private Integer customer;
    private List<Integer> cleaningProfessionals;
    private LocalDateTime startTime;
    private Integer duration;
    private LocalDateTime endTime;
}
