package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.dto.AnimalDto;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.service.mapper.AnimalMapper;
import com.coderiders.happyanimal.service.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class AnimalService {
    private AnimalRepository animalRepository;
    private UserRepository userRepository;
    private AnimalMapper animalMapper;
    private TaskMapper taskMapper;

    @Transactional
    public AnimalDto saveAnimal(AnimalDto animalDto, Long userId) {
        Animal animal = animalMapper.toAnimal(animalDto);
        animal.setUser(userRepository.getById(userId));
        animalRepository.save(animal);
        return animalMapper.toDto(animalRepository.findFirstById(animal.getId()).orElseThrow());
    }

    @Transactional
    public List<AnimalDto> getAllByUserId(Long userId) {
        List<Animal> found = animalRepository.findAllByUser(userRepository.getById(userId));
        return animalMapper.toDtoList(found);
    }

    @Transactional
    public List<AnimalDto> getAll() {
        return animalRepository.findAll().stream()
                .map(animal -> animalMapper.toDto(animal))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<TaskRsDto> getAnimalTasks(Long animalId){
        return getById(animalId)
                .getTasks()
                .stream()
                .map(task -> taskMapper.toRsDto(task))
                .collect(Collectors.toList());
    }

    @Transactional
    public Animal getById(Long id) {
        return animalRepository.findFirstById(id).orElse(null);
    }
}
