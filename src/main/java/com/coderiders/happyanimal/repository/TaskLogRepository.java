package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.model.TaskLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface TaskLogRepository extends JpaRepository<TaskLog, Long> {
    Page<TaskLog> getAllByCompletedDateTimeBetween(Pageable pageable, LocalDateTime sinceDateTime, LocalDateTime untilDateTime);
}
