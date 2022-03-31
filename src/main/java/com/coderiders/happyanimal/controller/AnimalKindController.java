package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import com.coderiders.happyanimal.service.AnimalKindService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/animalKinds")

@Tag(name = "animal-kind-controller", description = "Контроллер вида животного\n Класс, отряд и вид в отдельной таблице")
public class AnimalKindController {

    AnimalKindService service;

    @Autowired
    public AnimalKindController(AnimalKindService service) {
        this.service = service;
    }

    @Operation(summary = "Загружает заготовки видов в бд")
    @PostMapping
    public void createAll() throws IOException {
        service.createAll();
    }

    @Operation(summary = "Все виды (Объекты) из базы")
    @GetMapping(path = "/all")
    public List<AnimalKindDto> getAllKinds() {
        return service.getAll();
    }

    @Operation(summary = "Все строки с названиями видов")
    @GetMapping
    public List<String> getAllKindnames() {
        return service.getAllOnlyKinds();
    }

    @Operation(summary = "Возвращает объект по названию вида")
    @GetMapping(path = "/{kindname}")
    public AnimalKindDto getOneByKindName(@PathVariable String kindname) {
        return service.getByKindName(kindname);
    }

    @Operation(summary = "Сбрасывает таблицу видов")
    @DeleteMapping
    public void deleteAll() {
        service.deleteAll();
    }
}