package com.coderiders.happyanimal.mapper;

import com.coderiders.happyanimal.model.TaskLog;
import com.coderiders.happyanimal.model.dto.TaskLogRsDto;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class TaskLogMapper {
    @Transactional
    public TaskLogRsDto toRsDto(TaskLog taskLog) {
        return TaskLogRsDto.builder()
                .id(taskLog.getId())
                .taskId(taskLog.getTaskId())
                .userId(taskLog.getUserId())
                .taskType(taskLog.getTaskType().getType())
                .expiresDateTime(taskLog.getExpiresDateTime().toString())
                .completedDateTime(taskLog.getCompletedDateTime().toString())
                .repeatType(taskLog.getRepeatType().getTypeName())
                .animalId(taskLog.getAnimal().getId())
                .animalName(taskLog.getAnimal().getName())
                .animalKind(taskLog.getAnimal().getAnimalKind().getKind())
                .note(taskLog.getNote())
                .build();
    }
}
