package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.model.Inspection;
import com.coderiders.happyanimal.model.Report;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.repository.ReportRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class ReportRepTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private ReportRepository reportRepository;

    User user = User.builder()
            .age(47)
            .name("Иван")
            .build();
    Report report = Report.builder().date(LocalDate.now().toString()).text("Все ок").user(user).build();


    @Test
    @Transactional
    void findAll(){
        entityManager.persist(user);
        entityManager.persist(new Report());
        entityManager.persist(report);
        List<Report> found = reportRepository.findAll();
        assertEquals(found.size(), 2);
    }


    @Test
    @Transactional
    void findById(){
        Report persist = entityManager.persist(report);
        assertEquals(reportRepository.findById(1L).orElse(report), report);
    }
    @Test
    @Transactional
    void findAllByUser(){
        entityManager.persist(user);
        entityManager.persist(report);
        Page<Report> found = reportRepository.findAllByUser(user, Pageable.unpaged());
        assertEquals(found.getSize(),1);

    }
}
