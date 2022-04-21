package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/tasks")
@SecurityRequirement(name = "swagger_config")
@Tag(name = "task-controller", description = "задачи, связанные с животными")
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    //АДМИН
    @Operation(summary = "Добавление задачи",
            description = "Внимание, тип повторения должен быть одним из существующих")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TaskRsDto> addTask(@Validated @RequestBody TaskRqDto taskDto) {
        var created = taskService.saveTask(taskDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    //АДМИН, ЮЗЕР
    @Operation(summary = "Все задачи",
            description = "Если одно из полей представлено, то искать будет только по нему")
    @GetMapping
    public Page<TaskRsDto> getAllTasks(Pageable pageable, @RequestParam(required = false) Long userId,
                                       @RequestParam(required = false) Long animalId) {
        return taskService.getAll(pageable, userId, animalId);
    }

    //АДМИН, ЮЗЕР
    @Operation(summary = "Задача по её ID")
    @GetMapping(path = "/{taskId}")
    public TaskRsDto getTaskById(@PathVariable Long taskId) {
        return taskService.getById(taskId);
    }

    //АДМИН, юзеру без надобности
    @Operation(summary = "Все типы повторения")
    @GetMapping(path = "/repeat-types")
    public List<String> getAllRepeatTypes() {
        return RepeatType.getValues();
    }

    //АДМИН, ЮЗЕР (может менять статус на "сделано")
    @PutMapping(path = "/{id}")
    public ResponseEntity<TaskRsDto> updateTask(@PathVariable Long id, @RequestBody TaskRqDto taskRqDto) {
        var updated = taskService.updateTask(id, taskRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updated.getId())
                .toUri();
        return ResponseEntity.created(url).body(updated);
    }
}
