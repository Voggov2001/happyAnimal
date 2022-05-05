package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.model.*;
import com.coderiders.happyanimal.repository.InspectionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class InspectionRepTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InspectionRepository inspectionRepository;

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
    Inspection inspection = Inspection.builder().date(LocalDate.now()).animalList(Arrays.asList(animal)).build();

    @Test
    @Transactional
    void findAll() {
        entityManager.persist(new Inspection());
        List<Inspection> list = inspectionRepository.findAll();
        assertEquals(list.size(), 1);
    }

    @Test
    @Transactional
    void findById() {
        entityManager.persist(animalKind1);
        entityManager.persist(animal);
        Inspection persist = entityManager.persist(inspection);
        assertEquals(inspectionRepository.findById(1L).orElse(inspection), persist);
    }

    @Test
    @Transactional
    void findByDate(){
        entityManager.persist(user);
        entityManager.persist(animalKind1);
        entityManager.persist(animal);
        Inspection persist = entityManager.persist(inspection);
        assertEquals(inspectionRepository.findByDate(LocalDate.now()).orElse(inspection), persist);

    }
}
