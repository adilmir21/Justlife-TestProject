package com.example.justlife.dtos;

import com.example.justlife.models.CleaningProfessionalEntity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleDTO {
    private Integer id;
    private String vehicleNumber;
    private List<Integer> cleaningProfessionals = new ArrayList<>();
}
