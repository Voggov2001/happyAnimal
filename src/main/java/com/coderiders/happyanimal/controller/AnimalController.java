package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@Validated
@RestController
@RequestMapping("/animals")
@Tag(name = "animal-controller", description = "Контроллер животных\n Поля вида и статуса должны быть уже существующими")
public class AnimalController {
    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Добавление животного")
    @PostMapping
    public ResponseEntity<AnimalRsDto> addAnimal(@Valid @RequestBody AnimalRqDto animalRqDto) {
        var created = animalService.saveAnimal(animalRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @Operation(summary = "Выдача всех животных")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AnimalRsDto> getAllAnimals(Pageable pageable) {
        return animalService.getAll(pageable);
    }

    @Operation(summary = "Животные конкретного пользователя")
    @GetMapping(path = "/user/{userId}")
    public Page<AnimalRsDto> getUserAnimals(@PathVariable @Parameter(name = "User Id", example = "1") Long userId,
                                            Pageable pageable) {
        return animalService.getAllByUserId(userId, pageable);
    }

    @Operation(summary = "Задачи, прикрепленные к животным")
    @GetMapping(path = "/{animalId}/tasks")
    public Page<TaskRsDto> getAnimalTasks(@PathVariable Long animalId,
                                          Pageable pageable) {
        return animalService.getAnimalAllTasks(animalId, pageable);
    }

    @Operation(summary = "Изменить животное")
    @PutMapping(path = "/{animalId}")
    public AnimalRsDto editAnimal(@PathVariable Long animalId, @RequestBody AnimalRqDto animalRqDto) {
        return animalService.editAnimal(animalId, animalRqDto);
    }

    @Operation(summary = "Одно животное по id")
    @GetMapping(path = "/{animalId}")
    public AnimalRsDto getById(@PathVariable Long animalId) {
        return animalService.getById(animalId);
    }
}
