package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.TaskRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import com.coderiders.happyanimal.service.mapper.TaskMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TaskService {
    private TaskRepository taskRepository;
    private UserRepository userRepository;
    private AnimalRepository animalRepository;
    private TaskMapper taskMapper;

    @Transactional
    public void saveTask(TaskRqDto taskDto) {
        taskRepository.save(taskMapper.mapToTask(taskDto));
    }

    @Transactional
    public List<TaskRqDto> getAll() {
        return taskMapper.mapTaskListToDto(taskRepository.findAll());
    }

    @Transactional
    public List<List<TaskRqDto>> getByUserId(Long userId) {
        return animalRepository.findAllByUser(userRepository.getById(userId))
                .stream()
                .map(animal -> taskMapper.mapTaskListToDto(animal.getTasks()))
                .collect(Collectors.toList());
    }
}
