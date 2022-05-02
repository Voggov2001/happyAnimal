package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import com.coderiders.happyanimal.service.AnimalKindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/animalKinds")

@SecurityRequirement(name = "swagger_config")
@Tag(name = "animal-kind-controller", description = "Контроллер вида животного\n Класс, отряд и вид в отдельной таблице")
public class AnimalKindController {
    private final AnimalKindService animalKindService;

    @Autowired
    public AnimalKindController(AnimalKindService service) {
        this.animalKindService = service;
    }

    //АДМИН
    @Operation(summary = "Все виды (Объекты) из базы")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping
    public List<AnimalKindDto> getAllKinds() {
        return animalKindService.getAll();
    }

    //АДМИН
    @Operation(summary = "Все строки с названиями видов")
    @PreAuthorize("hasAuthority('admin')")
    @GetMapping(path = "only-kinds")
    public List<String> getAllKindNames() {
        return animalKindService.getAllOnlyKinds();
    }
}
