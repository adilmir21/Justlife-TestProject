package com.example.justlife.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity(name = "vehicle")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class VehicleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true)
    private String vehicleNumber;

    @OneToMany(mappedBy = "vehicle")
    private List<CleaningProfessionalEntity> cleaningProfessionals = new ArrayList<>();
}
