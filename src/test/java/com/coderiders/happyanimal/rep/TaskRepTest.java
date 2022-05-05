package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.exceptions.NotFoundException;
import com.coderiders.happyanimal.model.*;
import com.coderiders.happyanimal.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class TaskRepTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private TaskRepository taskRepository;

    @Test
    @Transactional
    void findAll(){
        Task persist = entityManager.persist(new Task());
        Page<Task> found = taskRepository.findAll(Pageable.unpaged());
        assertEquals(found.getSize(), 1);
    }

    @Test
    @Transactional
    void findById(){
        Task persist = entityManager.persist(new Task());
        assertEquals(taskRepository.findById(1L).orElse(taskRepository.getById(persist.getId())), persist);
    }
}
