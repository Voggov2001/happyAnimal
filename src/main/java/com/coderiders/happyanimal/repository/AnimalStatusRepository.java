package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.AnimalStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.xml.transform.sax.SAXResult;
import java.util.List;
import java.util.Optional;

@Repository
public interface AnimalStatusRepository extends JpaRepository<AnimalStatus, String> {
    Page<AnimalStatus> findAll(Pageable pageable);
    Optional<AnimalStatus> findById(String id);
    List<AnimalStatus> findByPermissionToParticipate(boolean permissionToParticipate);
}
