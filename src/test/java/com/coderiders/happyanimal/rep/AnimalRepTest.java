package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.enums.RepeatType;
import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.model.*;
import com.coderiders.happyanimal.repository.AnimalKindRepository;
import com.coderiders.happyanimal.repository.AnimalRepository;
import com.coderiders.happyanimal.repository.UserRepository;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.models.Pageable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class AnimalRepTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private AnimalRepository animalRepository;


    AnimalKind animalKind1 = new AnimalKind("Жираф", "Млекопитающие", "dsfsdfsmfk", "Парнокопытные".getBytes(StandardCharsets.UTF_8));

    User user = User.builder()
            .age(47)
            .name("Иван")
            .build();

    Animal animal = Animal.builder()
            .name("Шарик")
            .gender("male")
            .age(15)
            .height(155)
            .weight(228)
            .animalKind(animalKind1)
            .featuresOfKeeping("Пятно на лбу")
            .externalFeatures("закрытый вальер")
            .status(AnimalStatus.FINE)
            .location("Вальер1")
            .user(user)
            .build();

    @Test
    @Transactional
    void findAll() {
        entityManager.persist(animalKind1);
        entityManager.persist(user);
        entityManager.persist(animal);
        List<Animal> found = animalRepository.findAll();
        assertEquals(1, found.size());
    }

    @Test
    @Transactional
     void findById(){
        entityManager.persist(animalKind1);
        entityManager.persist(user);
        Animal animal1 = entityManager.persist(animal);
        assertEquals(animalRepository.findById(1L).orElse(animal), animal1);
    }


}
