package com.coderiders.happyanimal.services;


import com.coderiders.happyanimal.mapper.AnimalKindMapper;
import com.coderiders.happyanimal.model.AnimalKind;
import com.coderiders.happyanimal.model.dto.AnimalKindDto;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.service.AnimalKindService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;


@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class AnimalKindServiceTest {

    @Autowired
    private AnimalKindService animalKindService;

    @Autowired
    private AnimalKindRepository animalKindRepository;

    @Autowired
    private AnimalKindMapper animalKindMapper;

   /* @Autowired
    private TestEntityManager entityManager;*/
    //15

    AnimalKind animalKind1 = new AnimalKind("Жираф", "Млекопитающие", "dsfsdfsmfk", "Парнокопытные".getBytes(StandardCharsets.UTF_8));


    @Test
    @Transactional
    void getAll() {
        List<AnimalKindDto> list1 = animalKindService.getAll();
        assertEquals(list1.size(),15);
    }

    @Test
    @Transactional
    void getKindByName(){
        assertEquals(animalKindService.getByKindName(animalKind1.getKind()).getKind(), animalKind1.getKind());
    }
    @Test
    @Transactional
    void getAllOnlyKinds(){
        List<String> found = animalKindService.getAllOnlyKinds();
        assertEquals(found.size(), 15);
    }
}
