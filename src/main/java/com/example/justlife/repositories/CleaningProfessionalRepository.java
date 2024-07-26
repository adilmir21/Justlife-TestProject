package com.example.justlife.repositories;

import com.example.justlife.models.CleaningProfessionalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CleaningProfessionalRepository extends JpaRepository<CleaningProfessionalEntity, Integer> {
}
