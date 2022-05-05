package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.model.AnimalKind;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
@Getter
public class AnimalKindRepTest {
    @Autowired
    private TestEntityManager testEntityManager;

    @Autowired
    private AnimalKindRepository animalKindRepository;

    AnimalKind animalKind1 = new AnimalKind("Жираф", "Млекопитающие", "dsfsdfsmfk", "Парнокопытные".getBytes(StandardCharsets.UTF_8));
    AnimalKind animalKind2 = new AnimalKind("Зебра", "Млекопитающие", "ваыоалоывоа", "Непарнокопытные".getBytes(StandardCharsets.UTF_8));

    @Test
    @Transactional
    void findAll() {
        testEntityManager.persist(animalKind1);
        testEntityManager.persist(animalKind2);
        List<AnimalKind> foundList = animalKindRepository.findAll();
        assertEquals(2, foundList.size());
        assertTrue(foundList.contains(animalKind1) && foundList.contains(animalKind2));
    }
}
