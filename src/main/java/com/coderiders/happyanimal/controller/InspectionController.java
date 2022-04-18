//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping({"/inspection"})
public class InspectionController {
    private final InspectionService inspectionService;

    @Autowired
    public InspectionController(InspectionService service) {
        this.inspectionService = service;
    }

    @Operation(summary = "Записать животное на осмотр")
    @PutMapping(produces = {"application/json"})
    public ResponseEntity<Void> addAnimalToInspection(Long animalId, LocalDate localDate) {
        inspectionService.addAnimalToInspection(animalId, localDate);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Все запланированные осмотры")
    @GetMapping(produces = {"application/json"})
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        return this.inspectionService.getAll(pageable);
    }

    @Operation(summary = "Осмотр по id")
    @GetMapping(path = {"/{id}"})
    public InspectionRsDto getById(@PathVariable Long id) {
        return this.inspectionService.getById(id);
    }
}
