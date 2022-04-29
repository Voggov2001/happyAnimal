package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.mapper.TaskLogMapper;
import com.coderiders.happyanimal.model.dto.TaskLogRsDto;
import com.coderiders.happyanimal.repository.TaskLogRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

@Slf4j
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
    public Page<TaskLogRsDto> getAll(Pageable pageable, String startDate, String endDate) {
        Optional<String> dtStartString = Optional.ofNullable(startDate);
        Optional<String> dtEndString = Optional.ofNullable(endDate);
        LocalDate dtStart;
        LocalDate dtEnd;

        if (dtStartString.isPresent() && dtEndString.isPresent()) {
            dtStart = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            dtEnd = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
            return getAllByDateBetween(pageable, dtStart, dtEnd);
        } else if (dtStartString.isPresent()) {
            dtStart = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
            return getAllByDate(pageable, dtStart);
        } else if (dtEndString.isPresent()) {
            dtEnd = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
            return getAllByDate(pageable, dtEnd);
        } else {
            return taskLogRepository.findAll(pageable).map(taskLogMapper::toRsDto);
        }
    }

    @Transactional
    public Page<TaskLogRsDto> getAllByDate(Pageable pageable, LocalDate localDate) {
        return getAllByDateBetween(pageable, localDate, localDate);
    }

    @Transactional
    public Page<TaskLogRsDto> getAllByDateBetween(Pageable pageable, LocalDate sinceDate, LocalDate untilDate) {
        LocalDateTime todayStart = LocalDateTime.of(sinceDate.getYear(), sinceDate.getMonth(), sinceDate.getDayOfMonth(), 0, 0);
        LocalDateTime todayEnd = LocalDateTime.of(untilDate.getYear(), untilDate.getMonth(), untilDate.getDayOfMonth(), 23, 59);
        return taskLogRepository.getAllByCompletedDateTimeBetween(pageable, todayStart, todayEnd).map(taskLogMapper::toRsDto);

    }
}
