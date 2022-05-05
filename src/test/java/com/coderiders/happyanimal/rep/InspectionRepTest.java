package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.repository.InspectionRepository;
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
public class InspectionRepTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private InspectionRepository inspectionRepository;

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
        Inspection persist = entityManager.persist(new Inspection());
        assertEquals(inspectionRepository.findById(1L).orElse(persist), persist);
    }

    @Test
    @Transactional
    void findByDate() {
        Inspection persist = entityManager.persist(Inspection.builder().date(LocalDate.now()).build());
        assertEquals(inspectionRepository.findByDate(LocalDate.now()).orElse(persist), persist);

    }
}
