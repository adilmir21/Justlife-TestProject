package com.example.justlife.dataloaders;

import com.example.justlife.models.CleaningProfessionalEntity;
import com.example.justlife.models.VehicleEntity;
import com.example.justlife.repositories.CleaningProfessionalRepository;
import com.example.justlife.repositories.VehicleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class DataLoader {

    private final VehicleRepository vehicleRepository;
    private final CleaningProfessionalRepository cleaningProfessionalRepository;

    @Bean
    public CommandLineRunner loadData() {
        return args -> {
            log.info("DataLoader :: run starts");

            if (vehicleRepository.count() == 0 && cleaningProfessionalRepository.count() == 0) {
                log.info("No data found. Creating sample data.");

                List<VehicleEntity> vehicles = new ArrayList<>();
                List<CleaningProfessionalEntity> professionals = new ArrayList<>();

                // Create 5 vehicles
                IntStream.range(1, 6).forEach(i -> {
                    VehicleEntity vehicle = new VehicleEntity();
                    vehicle.setVehicleNumber("Vehicle-" + i);
                    vehicles.add(vehicleRepository.save(vehicle));

                    // Create 5 cleaning professionals for each vehicle
                    IntStream.range(1, 6).forEach(j -> {
                        CleaningProfessionalEntity professional = new CleaningProfessionalEntity();
                        professional.setName("Professional-" + i + "-" + j);
                        professional.setVehicle(vehicle);
                        professionals.add(professional);
                    });
                });

                cleaningProfessionalRepository.saveAll(professionals);

                log.info("Created {} vehicles and {} cleaning professionals", vehicles.size(), professionals.size());
            } else {
                log.info("Data already exists. Skipping data creation.");
            }

            log.info("DataLoader :: run ends");
        };
    }
}
