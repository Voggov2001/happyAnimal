package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.repository.AnimalStatusRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class AnimalMapper {
    private final UserRepository userRepository;
    private final AnimalKindRepository animalKindRepository;
    private final AnimalKindMapper animalKindMapper;
    private final UserMapper userMapper;
    private final AnimalStatusMapper animalStatusMapper;
    private final AnimalStatusRepository animalStatusRepository;

    @Autowired
    public AnimalMapper(UserRepository userRepository,
                        AnimalKindRepository animalKindRepository,
                        AnimalKindMapper animalKindMapper,
                        UserMapper userMapper,
                        AnimalStatusMapper animalStatusMapper,
                        AnimalStatusRepository animalStatusRepository) {
        this.userRepository = userRepository;
        this.animalKindRepository = animalKindRepository;
        this.animalKindMapper = animalKindMapper;
        this.userMapper = userMapper;
        this.animalStatusMapper = animalStatusMapper;
        this.animalStatusRepository = animalStatusRepository;
    }

    @Transactional
    public Animal mapToAnimal(AnimalRqDto dto) {
        return Animal.builder()
                .name(dto.getName())
                .gender(dto.getGender())
                .age(dto.getAge())
                .height(dto.getHeight())
                .weight(dto.getWeight())
                .animalKind(animalKindRepository.findById(dto.getKind()).orElseThrow(() -> new NotFoundException("Вид не найден")))
                .featuresOfKeeping(dto.getFeaturesOfKeeping())
                .externalFeatures(dto.getExternalFeatures())
                .status(animalStatusRepository.findById(dto.getStatus()).orElseThrow(() -> new NotFoundException("Статус не найден")))
                .location(dto.getLocation())
                .user(userRepository.findById(dto.getUserId()).orElse(null))
                .build();
    }

    public AnimalRsDto mapToDto(Animal animal) {
        AnimalRsDto animalRsDto = AnimalRsDto.builder()
                .id(animal.getId())
                .name(animal.getName())
                .age(animal.getAge())
                .gender(animal.getGender())
                .height(animal.getHeight())
                .weight(animal.getWeight())
                .animalKindDto(animalKindMapper.mapToDto(animal.getAnimalKind()))
                .location(animal.getLocation())
                .featuresOfKeeping(animal.getFeaturesOfKeeping())
                .externalFeatures(animal.getExternalFeatures())
                .build();
        if (animal.getUser() != null) {
            animalRsDto.setUserRsDto(userMapper.mapToResponseDto(animal.getUser()));
        }
        if (animal.getStatus() != null) {
            animalRsDto.setAnimalStatusRsDto(animalStatusMapper.mapToDto(animal.getStatus()));
        }
        return animalRsDto;
    }
}
