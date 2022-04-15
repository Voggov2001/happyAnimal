package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.model.TaskType;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.model.dto.TaskRsDto;
import com.coderiders.happyanimal.repository.AnimalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.format.DateTimeFormatter;

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
                .expiresDateTime(dto.getExpiresDateTime())
                .completed(dto.isCompleted())
                .repeatType(RepeatType.getByString(dto.getRepeatType()))
                .animal(animalRepository.findById(dto.getAnimalId()).orElseThrow(()->new NotFoundException("Животное не найдено")))
                .note(dto.getNote())
                .build();
    }

    @Transactional
    public TaskRsDto mapToRsDto(Task task) {
        return TaskRsDto.builder()
                .id(task.getId())
                .type(task.getTaskType().getType())
                .expiresDateTime(task.getExpiresDateTime().format(DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")))
                .completed(task.isCompleted())
                .repeatType(task.getRepeatType().getTypeName())
                .animalId(task.getAnimal().getId())
                .animalName(task.getAnimal().getName())
                .animalKind(task.getAnimal().getAnimalKind().getKind())
                .note(task.getNote())
                .build();
    }
}
