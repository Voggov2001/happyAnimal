package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AnimalMapper {
    UserRepository userRepository;
    AnimalKindRepository animalKindRepository;
    AnimalKindMapper animalKindMapper;
    UserMapper userMapper;

    @Autowired
    public AnimalMapper(UserRepository userRepository,
                        AnimalKindRepository animalKindRepository,
                        AnimalKindMapper animalKindMapper,
                        UserMapper userMapper) {
        this.userRepository = userRepository;
        this.animalKindRepository = animalKindRepository;
        this.animalKindMapper = animalKindMapper;
        this.userMapper = userMapper;
    }

    public Animal mapToAnimal(AnimalRqDto dto) {
        return Animal.builder()
                .name(dto.getName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .animalKind(animalKindRepository.getById(dto.getKind()))
                .featuresOfKeeping(dto.getFeaturesOfKeeping())
                .externalFeatures(dto.getExternalFeatures())
                .build();

    }

    public AnimalRsDto mapToDto(Animal animal) {
        return AnimalRsDto.builder()
                .id(animal.getId())
                .name(animal.getName())
                .gender(animal.getGender())
                .height(animal.getHeight())
                .weight(animal.getWeight())
                .animalKindDto(animalKindMapper.mapToDto(animal.getAnimalKind()))
                .location(animal.getLocation())
                .status(animal.getStatus())
                .userRsDto(userMapper.mapToResponseDto(animal.getUser()))
                .featuresOfKeeping(animal.getFeaturesOfKeeping())
                .externalFeatures(animal.getExternalFeatures())
                .build();
    }
}
