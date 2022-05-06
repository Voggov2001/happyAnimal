package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.model.Exhibition;
import com.coderiders.happyanimal.repository.ExhibitionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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

        Exhibition persist = entityManager.persist(new Exhibition());
        assertEquals(exhibitionRepository.findById(1L).orElse(exhibitionRepository.getById(persist.getId())), persist);
    }

    @Test
    @Transactional
    void findByDate() {
        Exhibition persist = entityManager.persist(Exhibition.builder().date(LocalDate.now()).build());
        assertEquals(exhibitionRepository.findByDate(LocalDate.now()).get(), persist);

    }
}
