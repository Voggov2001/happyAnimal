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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping({"/inspection"})
public class InspectionController {
    private final InspectionService inspectionService;

    @Autowired
    public InspectionController(InspectionService service) {
        this.inspectionService = service;
    }

    @PutMapping(path = "/{animalId}")
    public InspectionRsDto addAnimal(String localDate, @PathVariable Long animalId) {
        return inspectionService.addAnimalToInspection(localDate, animalId);
    }

    @PutMapping(path = "/-/{animalId}")
    public InspectionRsDto deleteAnimal(String localDate, @PathVariable Long animalId){
        return inspectionService.deleteAnimal(localDate, animalId);
    }

    @PutMapping
    public InspectionRsDto updateInspection(InspectionRqDto inspectionRqDto) {
        return inspectionService.update(inspectionRqDto);
    }

    @Operation(summary = "Все запланированные осмотры")
    @GetMapping(produces = {"application/json"})
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        return inspectionService.getAll(pageable);
    }

    @Operation(summary = "Осмотр по id")
    @GetMapping(path = {"/{id}"})
    public InspectionRsDto getById(@PathVariable Long id) {
        return this.inspectionService.getById(id);
    }

    @GetMapping(path = "/date/{date}")
    public InspectionRsDto getByDate(@PathVariable String date) {
        return inspectionService.getByDate(date);
    }

    @GetMapping(path = "/{id}/animals")
    public List<AnimalRsDto> getAnimals(@PathVariable Long id) {
        return inspectionService.getAnimals(id);
    }


}
