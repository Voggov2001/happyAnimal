package com.coderiders.happyanimal.rep;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.model.User;
import com.coderiders.happyanimal.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@TestPropertySource("/application.yml")
public class UserRepTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private UserRepository userRepository;


    @Test
    @Transactional
    void findById() {
        User persist = entityManager.persist(User.builder().name("Ilya").build());
        assertEquals(userRepository.findById(1L).orElse(persist), persist);
    }

    @Test
    @Transactional
    void findByLogin() {
        User persist = entityManager.persist(User.builder().name("Ильяз").login("ilyaz").build());
        assertEquals(userRepository.findByLogin("ilyaz").get(), persist);
    }

    @Test
    @Transactional
    void findAllByUserRole() {
        User user1 = entityManager.persist(User.builder().name("Афонасий").userRole(UserRole.EMPLOYEE).build());
        User user2 = entityManager.persist(User.builder().name("Григорий").userRole(UserRole.EMPLOYEE).build());
        Page<User> found = userRepository.findAllByUserRole(UserRole.EMPLOYEE, Pageable.unpaged());
        assertEquals(found.getSize(), 2);
    }
}
