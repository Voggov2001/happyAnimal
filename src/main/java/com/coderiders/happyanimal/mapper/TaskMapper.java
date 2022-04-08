package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.model.TaskType;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaskMapper {
    private final AnimalRepository animalRepository;

    @Autowired
    public TaskMapper(AnimalRepository animalRepository) {
        this.animalRepository = animalRepository;
    }

    @Transactional
    public Task mapToTask(TaskRqDto dto) {
        return Task.builder()
                .taskType(new TaskType(dto.getType()))
                .localDateTime(dto.getLocalDateTime())
                .completed(false)
                .repeatType(dto.getRepeatType())
                .animal(animalRepository.findById(dto.getAnimalId()).orElseThrow(()->new NotFoundException("Животное не найдено")))
                .build();
    }

    @Transactional
    public TaskRsDto mapToRsDto(Task task) {
        return TaskRsDto.builder()
                .id(task.getId())
                .type(task.getTaskType().getType())
                .dateTime(task.getLocalDateTime().toString())
                .completed(task.isCompleted())
                .repeatType(task.getRepeatType())
                .build();
    }
}
