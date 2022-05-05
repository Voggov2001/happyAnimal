package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.mapper.TaskMapper;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.model.TaskType;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.TaskRepository;
import com.coderiders.happyanimal.repository.TaskTypeRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.security.MyUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final AnimalRepository animalRepository;
    private final TaskMapper taskMapper;
    private final TaskTypeRepository taskTypeRepository;
    private static final String ERROR_MESSAGE_NOT_FOUND_TASK = "Задачка не найдена";
    private static final String ERROR_MESSAGE_NOT_FOUND_ANIMAL = "Зверь не найден";
    private static final String ERROR_MESSAGE_NOT_FOUND_USER = "Пользователь не найден";

    @Autowired
    public TaskService(TaskRepository taskRepository, UserRepository userRepository, AnimalRepository animalRepository, TaskMapper taskMapper, TaskTypeRepository taskTypeRepository) {
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.animalRepository = animalRepository;
        this.taskMapper = taskMapper;
        this.taskTypeRepository = taskTypeRepository;
    }

    @Transactional
    public TaskRsDto saveTask(TaskRqDto taskDto) {
        taskTypeRepository.save(new TaskType(taskDto.getType()));
        Task task = taskMapper.mapToTask(taskDto);
        return taskMapper.mapToRsDto(taskRepository.save(task));
    }

    @Transactional
    public Page<TaskRsDto> getAll(Pageable pageable, MyUserDetails userDetails, Long userId, Long animalId) {
        if (userDetails.getUserRole() == UserRole.EMPLOYEE) {
            return getByUserLogin(pageable, userDetails.getUsername());
        }
        if (Optional.ofNullable(userId).isPresent()) {
            return getByUserId(pageable, userId);
        }
        if (Optional.ofNullable(animalId).isPresent()) {
            return getByAnimalId(pageable, animalId);
        }
        Page<Task> allTasks = taskRepository.findAll(pageable);
        return allTasks.map(taskMapper::mapToRsDto);
    }

    @Transactional
    public Page<TaskRsDto> getByUserId(Pageable pageable, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_USER));
        Page<Animal> animals = animalRepository.findAllByUser(user, pageable);
        if (animals.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL);
        }
        List<TaskRsDto> tasks = getAnimalsTasks(animals);
        return new PageImpl<>(tasks, pageable, pageable.getOffset());
    }

    @Transactional
    public Page<TaskRsDto> getByUserLogin(Pageable pageable, String userLogin) {
        User user = userRepository.findByLogin(userLogin).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_USER));
        Page<Animal> animals = animalRepository.findAllByUser(user, pageable);
        if (animals.isEmpty()) {
            throw new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL);
        }
        List<TaskRsDto> tasks = getAnimalsTasks(animals);
        return new PageImpl<>(tasks, pageable, pageable.getOffset());
    }

    private List<TaskRsDto> getAnimalsTasks(Page<Animal> animals) {
        List<TaskRsDto> tasks = new ArrayList<>();
        animals.forEach(animal -> {
            List<Task> animalTasks = taskRepository.findAllByAnimal(animal);
            tasks.addAll(animalTasks.stream().map(taskMapper::mapToRsDto).collect(Collectors.toList()));
        });
        return tasks;
    }

    private Page<TaskRsDto> getByAnimalId(Pageable pageable, Long animalId) {
        Animal animal = animalRepository.findById(animalId).orElseThrow(
                () -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_ANIMAL));
        List<TaskRsDto> tasks = animal.getTasks()
                .stream()
                .map(taskMapper::mapToRsDto)
                .collect(Collectors.toList());
        return new PageImpl<>(tasks, pageable, pageable.getOffset());
    }

    public TaskRsDto updateTask(Long taskId, TaskRqDto taskRqDto) {
        taskTypeRepository.save(new TaskType(taskRqDto.getType()));
        Task task = taskMapper.mapToTask(taskRqDto);
        task.setId(taskId);
        return taskMapper.mapToRsDto(taskRepository.save(task));
    }

    public TaskRsDto getById(Long taskId) {
        return taskMapper.mapToRsDto(taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException(ERROR_MESSAGE_NOT_FOUND_TASK)));
    }
}