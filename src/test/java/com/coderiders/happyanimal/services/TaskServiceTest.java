package com.coderiders.happyanimal.services;


import com.coderiders.happyanimal.mapper.TaskMapper;
import com.coderiders.happyanimal.model.Task;
import com.coderiders.happyanimal.model.dto.TaskRqDto;
import com.coderiders.happyanimal.repository.TaskRepository;
import com.coderiders.happyanimal.service.TaskService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class TaskServiceTest {

    @Autowired
    private TaskService taskService;

    @MockBean
    private TaskRepository taskRepository;

    @MockBean
    private TaskMapper taskMapper;

    @Test
    void getById(){
        Optional<Task> optionalTask = Optional.of(Task.builder().id(1L).build());
        doReturn(optionalTask).when(taskRepository).findById(1L);
        taskService.getById(1L);
    }

    @Test
    void updateTask(){
        TaskRqDto taskRqDto = TaskRqDto.builder().animalId(1L).repeatType("Один раз").type("Убрать вальер").build();
        doReturn(Task.builder().id(1L).build()).when(taskMapper).mapToTask(ArgumentMatchers.any());
        taskService.updateTask(1L,taskRqDto);
    }
    @Test
    void saveTask(){
        TaskRqDto taskRqDto = TaskRqDto.builder().animalId(1L).repeatType("Один раз").type("Убрать вальер").build();
        doReturn(Task.builder().id(1L).build()).when(taskMapper).mapToTask(ArgumentMatchers.any());
        taskService.saveTask(taskRqDto);
        verify(taskRepository).save(ArgumentMatchers.any());
    }
}
