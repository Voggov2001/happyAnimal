package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.mapper.TaskLogMapper;
import com.coderiders.happyanimal.model.dto.TaskLogRsDto;
import com.coderiders.happyanimal.repository.TaskLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Service
public class TaskLogService {
    private final TaskLogRepository taskLogRepository;
    private final TaskLogMapper taskLogMapper;

    @Autowired
    public TaskLogService(TaskLogRepository taskLogRepository, TaskLogMapper taskLogMapper) {
        this.taskLogRepository = taskLogRepository;
        this.taskLogMapper = taskLogMapper;
    }

    @Transactional
    public Page<TaskLogRsDto> getAll(Pageable pageable, String startDateTime, String endDateTime) {
        Optional<String> dtStartString = Optional.ofNullable(startDateTime);
        Optional<String> dtEndString = Optional.ofNullable(endDateTime);
        LocalDateTime dtStart;
        LocalDateTime dtEnd;

        if (dtStartString.isPresent() && dtEndString.isPresent()) {
            dtStart = LocalDateTime.parse(startDateTime, DateTimeFormatter.ISO_DATE_TIME);
            dtEnd = LocalDateTime.parse(endDateTime, DateTimeFormatter.ISO_DATE_TIME);
            return getAllByDateBetween(pageable, dtStart, dtEnd);
        } else if (dtStartString.isPresent()) {
            dtStart = LocalDateTime.parse(startDateTime, DateTimeFormatter.ISO_DATE_TIME);
            return getAllByDate(pageable, dtStart);
        } else if (dtEndString.isPresent()) {
            dtEnd = LocalDateTime.parse(endDateTime, DateTimeFormatter.ISO_DATE_TIME);
            return getAllByDate(pageable, dtEnd);
        } else {
            return taskLogRepository.findAll(pageable).map(taskLogMapper::toRsDto);
        }
    }

    @Transactional
    public Page<TaskLogRsDto> getAllByDate(Pageable pageable, LocalDateTime localDateTime) {
        LocalDateTime todayStart = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 0, 0);
        LocalDateTime todayEnd = LocalDateTime.of(localDateTime.getYear(), localDateTime.getMonth(), localDateTime.getDayOfMonth(), 23, 59);
        return getAllByDateBetween(pageable, todayStart, todayEnd);
    }

    @Transactional
    public Page<TaskLogRsDto> getAllByDateBetween(Pageable pageable, LocalDateTime sinceDateTime, LocalDateTime untilDateTime) {
        return taskLogRepository.getAllByCompletedDateTimeBetween(pageable, sinceDateTime, untilDateTime)
                .map(taskLogMapper::toRsDto);

    }
}
