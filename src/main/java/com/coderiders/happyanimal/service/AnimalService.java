package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AnimalService {
    private final AnimalRepository animalRepository;
    private final UserRepository userRepository;
    private final AnimalMapper animalMapper;
    private static final String ERROR_MESSAGE_NOT_FOUND_ANIMAL = "Зверь не найден";

    @Autowired
    public AnimalService(AnimalRepository animalRepository,
                         UserRepository userRepository,
                         AnimalMapper animalMapper) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
        this.animalMapper = animalMapper;
    }

    @Transactional
    public AnimalRsDto saveAnimal(AnimalRqDto animalRqDto) {
        Animal animal = animalMapper.mapToAnimal(animalRqDto);
        return animalMapper.mapToDto(animalRepository.save(animal));
    }

    @Transactional
    public Page<AnimalRsDto> getAllByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        Page<Animal> allByUser = animalRepository.findAllByUser(user, pageable);
        if (allByUser.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL);
        }
        return allByUser.map(animalMapper::mapToDto);
    }

    @Transactional
    public Page<AnimalRsDto> getAllByUserLogin(Pageable pageable, String login) {
        User user = userRepository.findByLogin(login).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        Page<Animal> allByUser = animalRepository.findAllByUser(user, pageable);
        if (allByUser.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL);
        }
        return allByUser.map(animalMapper::mapToDto);
    }

    @Transactional
    public Page<AnimalRsDto> getAll(Pageable pageable, MyUserDetails userDetails, Long userId) {
        if (userDetails.getUserRole() == UserRole.EMPLOYEE) {
            return getAllByUserLogin(pageable, userDetails.getUsername());
        }
        if (userId == null) {
            Page<Animal> allAnimals = animalRepository.findAll(pageable);
            return allAnimals.map(animalMapper::mapToDto);
        }
        return getAllByUserId(pageable, userId);
    }

    @Transactional
    public AnimalRsDto getById(Long id) {
        Animal animal = animalRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        return animalMapper.mapToDto(animal);
    }

    @Transactional
    public AnimalRsDto editAnimal(Long animalId, AnimalRqDto animalRqDto) {
        if (!animalRepository.getById(animalId).getStatus().getName().equals(animalRqDto.getStatus())) {
            if (animalRqDto.getStatus().equals(AnimalStatus.DEAD.getName()) ||
                animalRqDto.getStatus().equals(AnimalStatus.SOLD.getName())) {
                List<Animal> animalList = userRepository.getById(animalRqDto.getUserId()).getAnimals();
                animalList.remove(animalMapper.mapToAnimal(animalRqDto));
                User user = userRepository.getById(animalRqDto.getUserId());
                user.setAnimals(animalList);
                userRepository.save(user);
            }
        }
        Animal animal = animalMapper.mapToAnimal(animalRqDto);
        animal.setId(animalId);
        return animalMapper.mapToDto(animalRepository.save(animal));
    }

    @Transactional
    public List<AnimalRsDto> getPermittedAnimals() {
        return animalRepository.findAll()
                .stream()
                .filter(animal -> animal.getStatus().isPermissionToParticipate())
                .map(animalMapper::mapToDto)
                .collect(Collectors.toList());
    }
}
