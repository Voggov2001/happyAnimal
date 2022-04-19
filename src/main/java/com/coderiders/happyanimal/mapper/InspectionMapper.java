package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Component
public class InspectionMapper {
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    @Autowired
    public InspectionMapper(AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    public Inspection mapToInspection(InspectionRqDto dto) {
        return Inspection.builder()
                .date(dto.getDate())
                .animalList(dto.getAnimalIdList()
                        .stream()
                        .map(aLong -> animalRepository
                                .findById(aLong)
                                .orElseThrow(() -> new NotFoundException("Животное не найдено")))
                        .collect(Collectors.toList()))
                .build();
    }

    public InspectionRsDto mapToRsDto(Inspection inspection) {
        return InspectionRsDto.builder()
                .id(inspection.getId())
                .date(inspection.getDate().format(DateTimeFormatter.ISO_DATE))
                .animalList(inspection.getAnimalList()
                        .stream()
                        .map(animalMapper::mapToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
