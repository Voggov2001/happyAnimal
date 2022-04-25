//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping({"/inspection"})
@SecurityRequirement(name = "swagger_config")
public class InspectionController {
    private final InspectionService inspectionService;

    @Autowired
    public InspectionController(InspectionService service) {
        this.inspectionService = service;
    }

    //ВСЕ
    @Operation(summary = "Добавить животное на осмотр")
    @PutMapping(path = "/{animalId}")
    public InspectionRsDto addAnimal(String localDate, @PathVariable Long animalId) {
        return inspectionService.addAnimalToInspection(localDate, animalId);
    }

    //АДМИН, ВЕТЕРИНАР
    @Operation(summary = "Обновить осмотр(Имхо бесполезная вещь)")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('veterinarian')")
    @PutMapping
    public InspectionRsDto updateInspection(InspectionRqDto inspectionRqDto) {
        return inspectionService.update(inspectionRqDto);
    }

    //ВЕТЕРИНАР, АДМИН
    @Operation(summary = "Все запланированные осмотры")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('veterinarian')")
    @GetMapping(produces = {"application/json"})
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        return inspectionService.getAll(pageable);
    }

    //АДМИН, ВЕТЕРИНАР
    @Operation(summary = "Осмотр по id")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('veterinarian')")
    @GetMapping(path = {"/{id}"})
    public InspectionRsDto getById(@PathVariable Long id) {
        return this.inspectionService.getById(id);
    }

    //АДМИН, ВЕТЕРИНАР
    @Operation(summary = "Осмотр по дате")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('veterinarian')")
    @GetMapping(path = "/date/{date}")
    public InspectionRsDto getByDate(@PathVariable String date) {
        return inspectionService.getByDate(date);
    }

    //АДМИН, ВЕТЕРИНАР
    @Operation(summary = "Животные одного осмотра")
    @PreAuthorize("hasAuthority('admin') || hasAuthority('veterinarian')")
    @GetMapping(path = "/{id}/animals")
    public List<AnimalRsDto> getAnimals(@PathVariable Long id) {
        return inspectionService.getAnimals(id);
    }
}
