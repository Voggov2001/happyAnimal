package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Inspection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface InspectionRepository extends JpaRepository<Inspection, Long> {
    Optional<Inspection> findById(Long id);

    Page<Inspection> findAll(Pageable pageable);

    Optional<Inspection> findByDate(LocalDate localDate);

    Optional<Inspection> findByAnimalListContaining(Animal animal);
}

