//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.service.InspectionService;
import io.swagger.v3.oas.annotations.Operation;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping({"/inspection"})
public class InspectionController {
    private InspectionService service;

    @Autowired
    public InspectionController(InspectionService service) {
        this.service = service;
    }

    @Operation(summary = "Добавление осмотра")
    @PostMapping(produces = {"application/json"})
    public ResponseEntity<InspectionRsDto> addInspection(@Validated @RequestBody InspectionRqDto dto) {
        InspectionRsDto created = this.service.saveInspection(dto);
        URI url = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(new Object[]{created.getId()}).toUri();
        return ResponseEntity.created(url).body(created);
    }

    @Operation(summary = "Все запланированные осмотры")
    @GetMapping(produces = {"application/json"})
    public Page<InspectionRsDto> getAll(Pageable pageable) {
        return this.service.getAll(pageable);
    }

    @Operation(summary = "Осмотр по id")
    @GetMapping(path = {"/{id}"})
    public InspectionRsDto getById(@PathVariable Long id) {
        return this.service.getById(id);
    }
}
