package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.service.TaskTypeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/tasktype")
@SecurityRequirement(name = "swagger_config")
@Tag(name = "task-type-controller", description = "контроллер для типа задачи")
public class TaskTypeController {

    private final TaskTypeService taskTypeService;

    @Autowired
    public TaskTypeController(TaskTypeService taskTypeService) {
        this.taskTypeService = taskTypeService;
    }

    //АДМИН
    @Operation(summary = "Добавление типа задачи")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addTaskType(@Validated @RequestParam String type) {
        String created = taskTypeService.createTaskType(type);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created)
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    //АДМИН
    @Operation(summary = "Все типы задач")
    @GetMapping (produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllTypes(){
        return taskTypeService.getAllTypes();
    }

}
