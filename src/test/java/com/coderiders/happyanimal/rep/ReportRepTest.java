package com.coderiders.happyanimal.rep;

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


    @Test
    @Transactional
    void findById() {
        Report persist = entityManager.persist(new Report());
        assertEquals(reportRepository.findById(1L).orElse(persist), persist);
    }

    @Test
    @Transactional
    void findAll() {
        entityManager.persist(new Report());
        entityManager.persist(new Report());
        List<Report> found = reportRepository.findAll();
        assertEquals(found.size(), 2);
    }


    @Test
    @Transactional
    void findAllByUser() {
        User user1 = entityManager.persist(User.builder().name("Вениамин").build());
        entityManager.persist(Report.builder().user(user1).build());
        Page<Report> found = reportRepository.findAllByUser(user1, Pageable.unpaged());
        assertEquals(found.getSize(), 1);

    }
}
