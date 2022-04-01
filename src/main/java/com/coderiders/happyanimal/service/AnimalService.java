package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.AnimalMapper;
import com.coderiders.happyanimal.mapper.TaskMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.AnimalRqDto;
import com.coderiders.happyanimal.model.dto.AnimalRsDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
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
    private final TaskMapper taskMapper;
    private static final String ERROR_MESSAGE_NOT_FOUND_ANIMAL = "Зверь не найден";
    private static final String ERROR_MESSAGE_NOT_FOUND_USER = "Пользователь не найден";

    @Autowired
    public AnimalService(AnimalRepository animalRepository,
                         UserRepository userRepository,
                         AnimalMapper animalMapper,
                         TaskMapper taskMapper) {
        this.animalRepository = animalRepository;
        this.userRepository = userRepository;
        this.animalMapper = animalMapper;
        this.taskMapper = taskMapper;
    }

    @Transactional
    public AnimalRsDto saveAnimal(AnimalRqDto animalRqDto) {
        Animal animal = animalMapper.mapToAnimal(animalRqDto);

        return animalMapper.mapToDto(animalRepository.save(animal));
    }

    @Transactional
    public Page<AnimalRsDto> getAllByUserId(Long userId, Pageable pageable) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        Page<Animal> allByUser = animalRepository.findAllByUser(user, pageable);
        if (allByUser.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL);
        }
        return allByUser.map(animalMapper::mapToDto);
    }

    @Transactional
    public Page<AnimalRsDto> getAll(Pageable pageable) {
        Page<Animal> allAnimals = animalRepository.findAll(pageable);
        return allAnimals.map(animalMapper::mapToDto);
    }

    @Transactional
    public Page<TaskRsDto> getAnimalAllTasks(Long animalId, Pageable pageable) {
        Animal animal = animalRepository.findById(animalId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        List<Task> taskList = animal.getTasks();
        return new PageImpl<>(taskList.stream()
                .map(taskMapper::mapToRsDto)
                .collect(Collectors.toList()),
                pageable, pageable.getOffset());
    }

    @Transactional
    public AnimalRsDto getById(Long id) {
        Animal animal = animalRepository.findById(id).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        return animalMapper.mapToDto(animal);
    }

    @Transactional
    public AnimalRsDto setUser(Long animalId, Long userId) {
        Animal animal = animalRepository.findById(animalId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL)
        );
        animal.setUser(userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_USER)
        ));
        animalRepository.save(animal);
        return animalMapper.mapToDto(animalRepository.save(animal));
    }
}
