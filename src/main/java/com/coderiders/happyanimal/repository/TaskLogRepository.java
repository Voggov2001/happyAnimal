package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
}
