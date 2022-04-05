package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.AnimalStatus;
import com.coderiders.happyanimal.model.dto.AnimalStatusRqDto;
import com.coderiders.happyanimal.model.dto.AnimalStatusRsDto;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AnimalStatusMapper {
    public AnimalStatus mapToAnimalStatus(AnimalStatusRqDto dto) {
        var mapper = new ModelMapper();
        return mapper.map(dto, AnimalStatus.class);
    }

    public AnimalStatusRsDto mapToDto(AnimalStatus animalStatus) {
        var mapper = new ModelMapper();
        return mapper.map(animalStatus, AnimalStatusRsDto.class);
    }
}
