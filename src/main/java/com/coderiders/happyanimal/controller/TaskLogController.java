package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.TaskLogRsDto;
import com.coderiders.happyanimal.service.TaskLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/taskLog")
@SecurityRequirement(name = "swagger_config")
@Tag(name = "task-log-controller", description = "выполненные задачи для автоотчетов")
public class TaskLogController {

    private final TaskLogService taskLogService;

    @Autowired
    public TaskLogController(TaskLogService taskLogService) {
        this.taskLogService = taskLogService;
    }

    //АДМИН
    @Operation(summary = "Все логи задач",
            description = "Если указана только одна из дат, то вернет на указанную дату\n" +
                    "Если указаны обе даты, то вернет в промежутке между ними\n" +
                    "Если даты не указаны, то вернет все логи")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public Page<TaskLogRsDto> getAll(Pageable pageable,
                                     @RequestParam(required = false) String startDate,
                                     @RequestParam(required = false) String endDate) {
        return taskLogService.getAll(pageable, startDate, endDate);
    }
}
