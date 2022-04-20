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
    private final AnimalKindService animalKindService;

    @Autowired
    public AnimalKindController(AnimalKindService service) {
        this.animalKindService = service;
    }

    //НИКТО
    @Operation(summary = "Загружает заготовки видов в бд")
    @PostMapping
    public void createAll() throws IOException {
        animalKindService.createAll();
    }

    //АДМИН
    @Operation(summary = "Все виды (Объекты) из базы")
    @GetMapping
    public List<AnimalKindDto> getAllKinds() {
        return animalKindService.getAll();
    }

    //АДМИН
    @Operation(summary = "Все строки с названиями видов")
    @GetMapping(path = "only-kinds")
    public List<String> getAllKindNames() {
        return animalKindService.getAllOnlyKinds();
    }

    //АДМИН, но он и не нужен никому по сути
    @Operation(summary = "Возвращает объект по названию вида")
    @GetMapping(path = "/{kindName}")
    public AnimalKindDto getOneByKindName(@PathVariable String kindName) {
        return animalKindService.getByKindName(kindName);
    }
}
