package com.example.justlife.models;

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
@Entity(name = "appointment")
public class AppointmentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private CustomerEntity customer;

    @ManyToMany
    @JoinTable(
            name = "appointment_cleaning_professional",
            joinColumns = @JoinColumn(name = "appointment_id"),
            inverseJoinColumns = @JoinColumn(name = "cleaning_professional_id")
    )
    private List<CleaningProfessionalEntity> cleaningProfessionals = new ArrayList<>();

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime startTime;

    private Integer duration;

    @Column(columnDefinition = "TIMESTAMP")
    private LocalDateTime endTime;

    @PrePersist
    @PreUpdate
    public void calculateEndTime() {
        this.endTime = this.startTime.plusHours(this.duration);
    }
}
