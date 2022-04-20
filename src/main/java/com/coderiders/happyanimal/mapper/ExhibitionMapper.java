package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.BadRequestException;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.model.dto.ExhibitionRqDto;
import com.coderiders.happyanimal.model.dto.ExhibitionRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ExhibitionMapper {
    private final AnimalRepository animalRepository;
    private final AnimalMapper animalMapper;

    public ExhibitionMapper(AnimalRepository animalRepository, AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.animalMapper = animalMapper;
    }

    public Exhibition mapToExhibition(ExhibitionRqDto dto) {
        return Exhibition.builder()
                .date(Optional.ofNullable(LocalDate.parse(dto.getDate(), DateTimeFormatter.ISO_DATE)).orElseThrow(()-> new BadRequestException("Дата некорректна")))
                .animals(dto.getAnimalIds()
                        .stream()
                        .map(aLong -> animalRepository
                                .findById(aLong)
                                .orElseThrow(() -> new NotFoundException("Животное не найдено")))
                        .collect(Collectors.toList()))
                .build();
    }

    public ExhibitionRsDto mapToRsDto(Exhibition exhibition) {
        return ExhibitionRsDto.builder()
                .id(exhibition.getId())
                .date(exhibition.getDate().format(DateTimeFormatter.ISO_DATE))
                .animals(exhibition.getAnimals()
                        .stream()
                        .map(animalMapper::mapToDto)
                        .collect(Collectors.toList()))
                .build();
    }
}
