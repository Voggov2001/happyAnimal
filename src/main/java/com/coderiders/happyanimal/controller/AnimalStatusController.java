package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalStatusRqDto;
import com.coderiders.happyanimal.model.dto.AnimalStatusRsDto;
import com.coderiders.happyanimal.service.AnimalStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
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
        var created = animalStatusService.saveAnimal(animalStatusDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AnimalStatusRsDto> getAllAnimals(Pageable pageable) {
        return animalStatusService.getAll(pageable);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity getAnimalStatusById(@PathVariable Long id) {
        var created = animalStatusService.getById(id);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @GetMapping
    public List<AnimalStatusRsDto> getByPermissionToParticipate(@RequestParam boolean permissionToParticipate) {
        return animalStatusService.getByPermissionToParticipate(permissionToParticipate);
    }
}
