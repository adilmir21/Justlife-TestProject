package com.example.justlife.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalTime;
import java.util.List;

@Entity(name = "cleaning_professional")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CleaningProfessionalEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @ManyToOne
    @JoinColumn(name = "vehicle_id")
    private VehicleEntity vehicle;

    @ManyToMany(mappedBy = "cleaningProfessionals")
    private List<AppointmentEntity> appointments;

    @Column(columnDefinition = "TIME")
    private LocalTime workStartTime = LocalTime.of(8, 0);

    @Column(columnDefinition = "TIME")
    private LocalTime workEndTime = LocalTime.of(22, 0);
}
