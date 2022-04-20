package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.service.AnimalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@RequestMapping("/animals")
@Tag(name = "animal-controller", description = "Контроллер животных\n Поля вида и статуса должны быть уже существующими")
public class AnimalController {
    private final AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    //АДМИН
    @Operation(summary = "Добавление животного")
    @PostMapping
    public ResponseEntity<AnimalRsDto> addAnimal(@Valid @RequestBody AnimalRqDto animalRqDto) {
        var created = animalService.saveAnimal(animalRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    //ЮЗЕРУ ПО ID, АДМИНУ МОЖНО И ТАК И ТАК ПРИ ЖЕЛАНИИ СДЕЛАТЬ, ВЕТЕРИНАРУ МОЖНО ОСТАВИТЬ
    @Operation(summary = "Выдача всех животных",
            description = "Если представлен id пользователся, то возвращает только животных конкретного поьзователя")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public Page<AnimalRsDto> getAllAnimals(Pageable pageable, Long userId) {
        return animalService.getAll(pageable, userId);
    }

    //АДМИН
    @Operation(summary = "Животные, разрешенные для выставки")
    @GetMapping(path = "/permitted")
    public List<AnimalRsDto> getPermittedAnimals(){
        return animalService.getPermittedAnimals();
    }

    //ВСЕ
    @Operation(summary = "Статусы/состояния животных")
    @GetMapping(path = "/states",produces = MediaType.APPLICATION_JSON_VALUE)
    public List<String> getAllAnimalStates() {
        return AnimalStatus.getAllStatusNames();
    }

    //ВСЕ
    @Operation(summary = "Изменить животное")
    @PutMapping(path = "/{animalId}")
    public AnimalRsDto editAnimal(@PathVariable Long animalId, @RequestBody AnimalRqDto animalRqDto) {
        return animalService.editAnimal(animalId, animalRqDto);
    }

    //ВСЕ
    @Operation(summary = "Одно животное по id")
    @GetMapping(path = "/{animalId}")
    public AnimalRsDto getById(@PathVariable Long animalId) {
        return animalService.getById(animalId);
    }
}
