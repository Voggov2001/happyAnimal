package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.Exhibition;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExhibitionRepository extends JpaRepository<Exhibition, Long> {
    Optional<Exhibition> findById(Long id);
    Page<Exhibition> findAll(Pageable pageable);
    Optional<Exhibition> findByDate(String date);
    void deleteExhibitionById(Long id);
}
