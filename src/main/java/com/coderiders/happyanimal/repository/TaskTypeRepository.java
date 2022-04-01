package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.TaskType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskTypeRepository extends JpaRepository<TaskType, String> {
}
