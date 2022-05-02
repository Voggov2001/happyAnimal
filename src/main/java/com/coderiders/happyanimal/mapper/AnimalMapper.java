package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.repository.InspectionRepository;
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
    private final InspectionRepository inspectionRepository;

    @Autowired
    public AnimalMapper(UserRepository userRepository,
                        AnimalKindRepository animalKindRepository,
                        AnimalKindMapper animalKindMapper,
                        UserMapper userMapper, InspectionRepository inspectionRepository) {
        this.userRepository = userRepository;
        this.animalKindRepository = animalKindRepository;
        this.animalKindMapper = animalKindMapper;
        this.userMapper = userMapper;
        this.inspectionRepository = inspectionRepository;
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
                .status(AnimalStatus.getByName(dto.getStatus()))
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
                .status(animal.getStatus().getName().equals(AnimalStatus.BOOKED_INSPECTION.getName())?
                        animal
                                .getStatus()
                                .getName() + " (" + inspectionRepository
                                .findByAnimalListContaining(animal)
                                .get()
                                .getDate() + ")"
                        : animal.getStatus().getName())
                .build();
        if (animal.getUser() != null) {
            animalRsDto.setUserRsDto(userMapper.mapToResponseDto(animal.getUser()));
        }
        return animalRsDto;
    }
}
