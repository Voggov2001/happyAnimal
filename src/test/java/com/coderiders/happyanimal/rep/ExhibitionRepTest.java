package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.enums.AnimalStatus;
import com.coderiders.happyanimal.model.Animal;
import com.coderiders.happyanimal.model.AnimalKind;
import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.repository.ExhibitionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class ExhibitionRepTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private ExhibitionRepository exhibitionRepository;

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
    Exhibition exhibition1 = Exhibition.builder().date(LocalDate.now()).animals(Arrays.asList(animal)).build();

    @Test
    @Transactional
    void findAll() {
        entityManager.persist(new Exhibition());
        List<Exhibition> list = exhibitionRepository.findAll();
        assertEquals(list.size(), 1);
    }

    @Test
    @Transactional
    void findById() {
        entityManager.persist(animalKind1);
        entityManager.persist(animal);
        Exhibition persist = entityManager.persist(exhibition1);
        assertEquals(exhibitionRepository.findById(1L).orElse(exhibition1), persist);
    }

    @Test
    @Transactional
    void findByDate(){
        entityManager.persist(user);
        entityManager.persist(animalKind1);
        entityManager.persist(animal);
        Exhibition persist = entityManager.persist(exhibition1);
        assertEquals(exhibitionRepository.findByDate(LocalDate.now()).orElse(exhibition1), persist);

    }
}
