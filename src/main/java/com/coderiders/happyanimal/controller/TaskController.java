package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.service.TaskService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
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
@Tag(name = "task-controller", description = "задачи, связанные с животными")
@Slf4j
public class TaskController {
    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

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

    @Operation(summary = "Задачи конкретного пользователя")
    @GetMapping(path = "/user/{userId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TaskRsDto> getUserTasks(@PathVariable Long userId, Pageable pageable) {
        return taskService.getByUserId(userId, pageable);
    }

    @Operation(summary = "Задачи конкретного животного")
    @GetMapping(path = "/animal/{animalId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TaskRsDto> getAnimalTasks(@PathVariable Long animalId, Pageable pageable) {
        return taskService.getByAnimalId(animalId, pageable);
    }

    @Operation(summary = "Все задачи")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<TaskRsDto> getAllTasks(Pageable pageable) {
        return taskService.getAll(pageable);
    }

    @Operation(summary = "Все типы повторения")
    @GetMapping(path = "/repeat-types")
    public List<String> getAllRepeatTypes(){
        return RepeatType.getValues();
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<TaskRsDto> updateTask(@PathVariable Long id, @RequestBody TaskRqDto taskRqDto){
        var updated = taskService.updateTask(id, taskRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(updated.getId())
                .toUri();
        return ResponseEntity.created(url).body(updated);
    }
}
