package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRqDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRsDto;
import com.coderiders.happyanimal.service.ExhibitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Validated
@RestController
@RequestMapping("/exhibitions")
@SecurityRequirement(name = "swagger_config")
@Tag(name = "exhibition-controller", description = "Выставки животных")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @Autowired
    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    //АДМИН
    @Operation(summary = "Создание выставки")
    @PreAuthorize("hasAuthority('admin')")
    @PostMapping
    public ResponseEntity<ExhibitionRsDto> createExhibition(ExhibitionRqDto exhibitionRqDto){
        var created = exhibitionService.saveExhibition(exhibitionRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    //АДМИН
    @Operation(summary = "Добавить животное на выставку")
    @PreAuthorize("hasAuthority('admin')")
    @PutMapping(path = "/{animalId}")
    public ExhibitionRsDto addAnimal(@RequestParam String localDate, @PathVariable Long animalId) {
        return exhibitionService.addAnimalToExhibition(localDate, animalId);
    }

    //АДМИН
    @Operation(summary = "Обновление выставки(Имхо не понадобится)")
    @PreAuthorize("hasAuthority('admin')")
    @PutMapping
    public ExhibitionRsDto updateInspection(ExhibitionRqDto inspectionRqDto) {
        return exhibitionService.update(inspectionRqDto);
    }

    //АДМИН, мб ЮЗЕР
    @Operation(summary = "Все запланированные выставки")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(produces = {"application/json"})
    public Page<ExhibitionRsDto> getAll(Pageable pageable) {
        return exhibitionService.getAll(pageable);
    }

    //АДМИН, мб ЮЗЕР
    @Operation(summary = "Выставка по id")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = {"/{id}"})
    public ExhibitionRsDto getById(@PathVariable Long id) {
        return exhibitionService.getById(id);
    }

    //АДМИН, мб ЮЗЕР
    @Operation(summary = "Выставка по дате")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/date/{date}")
    public ExhibitionRsDto getByDate(@PathVariable String date) {
        return exhibitionService.findByDate(date);
    }

    //АДМИН, мб ЮЗЕР
    @Operation(summary = "Животные одной выставки")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "/{id}/animals")
    public List<AnimalRsDto> getAnimals(@PathVariable Long id) {
        return exhibitionService.getAllAnimals(id);
    }
}
