package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.TaskLogRsDto;
import com.coderiders.happyanimal.service.TaskLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/taskLog")
@Tag(name = "task-log-controller", description = "выполненные задачи для автоотчетов")
public class TaskLogController {

    private final TaskLogService taskLogService;

    @Autowired
    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    @Operation(summary = "Все логи задач",
            description = "Если указана только одна из дат, то вернет на указанную дату\n" +
                    "Если указаны обе даты, то вернет в промежутке между ними\n" +
                    "Если даты не указаны, то вернет все логи")
    @GetMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public Page<TaskLogRsDto> getAll(Pageable pageable,
                                     String startDateTime,
                                     String endDateTime) {
        return taskLogService.getAll(pageable, startDateTime, endDateTime);
    }
}
