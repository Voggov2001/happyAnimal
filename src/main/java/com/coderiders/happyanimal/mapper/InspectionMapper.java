package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.dto.InspectionRqDto;
import com.coderiders.happyanimal.model.dto.InspectionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class InspectionMapper {
    private final AnimalRepository animalRepository;

    @Autowired
    public InspectionMapper(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
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
        ModelMapper mapper = new ModelMapper();
        return mapper.map(inspection, InspectionRsDto.class);
    }
}
