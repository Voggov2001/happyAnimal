package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalStatusRqDto;
import com.coderiders.happyanimal.model.dto.AnimalStatusRsDto;
import com.coderiders.happyanimal.service.AnimalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;

@Validated
@RestController
@RequestMapping("/animalStatuses")
public class AnimalStatusController {
    private final AnimalStatusService animalStatusService;

    @Autowired
    public AnimalStatusController(AnimalStatusService animalStatusService) {
        this.animalStatusService = animalStatusService;
    }

    @PostMapping
    public ResponseEntity<AnimalStatusRsDto> addAnimalStatus(@Valid @RequestBody AnimalStatusRqDto animalStatusDto) {
        var created = animalStatusService.saveAnimalStatus(animalStatusDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getName())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<AnimalStatusRsDto> getAnimalStatusById(@PathVariable String id) {
        var created = animalStatusService.getById(id);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getName())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @GetMapping("/{permissionToParticipate}")
    public List<AnimalStatusRsDto> getByPermissionToParticipate(@PathVariable boolean permissionToParticipate) {
        return animalStatusService.getByPermissionToParticipate(permissionToParticipate);
    }

    @GetMapping
    public Page<AnimalStatusRsDto> getAllStatuses(Pageable pageable) {
        return animalStatusService.getAll(pageable);
    }
}
