package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.AnimalKind;
import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;

@Component
public class AnimalKindMapper {

    public AnimalKind mapToAnimalKind(AnimalKindDto dto) {
        var mapper = new ModelMapper();
        return mapper.map(dto, AnimalKind.class);
    }

    public AnimalKindDto mapToDto(AnimalKind kind) {
        return AnimalKindDto.builder()
                .kind(kind.getKind())
                .animalClass(kind.getSquad())
                .squad(kind.getSquad())
                .pic(new String(kind.getPic(), StandardCharsets.UTF_8))
                .build();
    }
}
