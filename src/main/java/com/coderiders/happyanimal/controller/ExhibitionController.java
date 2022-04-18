package com.coderiders.happyanimal.controller;

import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRqDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRsDto;
import com.coderiders.happyanimal.service.ExhibitionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@Validated
@RestController
@RequestMapping("/exhibitions")
@Tag(name = "exhibition-controller", description = "Выставки животных")
public class ExhibitionController {
    private final ExhibitionService exhibitionService;

    @Autowired
    public ExhibitionController(ExhibitionService exhibitionService) {
        this.exhibitionService = exhibitionService;
    }

    @Operation(summary = "Создание выставки")
    @PostMapping
    public ResponseEntity<ExhibitionRsDto> addExhibition(@Validated @RequestParam ExhibitionRqDto exhibitionRqDto) {
        var created = exhibitionService.saveExhibition(exhibitionRqDto);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @Operation(summary = "Все существующие выставки")
    @GetMapping
    public Page<ExhibitionRsDto> getAllExhibitions(Pageable pageable) {
        return exhibitionService.getAll(pageable);
    }

    @Operation(summary = "Выставка на конкретную дату")
    @GetMapping(path = "/{date}")
    public ResponseEntity<ExhibitionRsDto> getExhibitionByDate(@PathVariable String date) {
        var created = exhibitionService.findByDate(date);
        var url = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();
        return ResponseEntity.created(url).body(created);
    }

    @Operation(summary = "Удаление выставки по id")
    @DeleteMapping(value = "/delete/{id}")
    public void deleteExhibitionById(@PathVariable Long id) {
        exhibitionService.deleteExhibitionById(id);
    }

    @Operation(summary = "Добавление животного на выставку")
    @PutMapping (value = "/add/{id}")
    public void addAnimalIntoExhibition(@RequestParam Long id) {
        exhibitionService.addAnimalIntoExhibition(id);
    }

    @Operation(summary = "Получение списка животных")
    @GetMapping("/all_animals/{id}")
    public List<AnimalRsDto> getAllAnimalFromExhibition(@PathVariable Long id) {
        return exhibitionService.getAllAnimals(id);
    }

    @Operation(summary = "Удаление животного из выставки")
    @DeleteMapping("delete/{id}/animal/{id}")
    public ExhibitionRsDto deleteAnimalFromExhibition(@RequestParam Long id,@RequestParam Long animalId){
        return exhibitionService.deleteAnimalFromExhibition(id, animalId);
    }
}
