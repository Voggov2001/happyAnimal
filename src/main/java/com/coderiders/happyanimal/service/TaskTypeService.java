package com.coderiders.happyanimal.service;

import com.coderiders.happyanimal.model.TaskType;
import com.coderiders.happyanimal.repository.TaskTypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskTypeService {
    private final TaskTypeRepository taskTypeRepository;

    @Autowired
    public TaskTypeService(TaskTypeRepository repository) {
        this.taskTypeRepository = repository;
    }

    @Transactional
    public String createTaskType(String type) {
        TaskType taskType = new TaskType(type);
        taskTypeRepository.save(taskType);
        return taskType.getType();
    }

    public List<String> getAllTypes() {
        return taskTypeRepository.findAll()
                .stream()
                .map(TaskType::getType)
                .collect(Collectors.toList());
    }
}
