package com.coderiders.happyanimal.repository;

import com.coderiders.happyanimal.enums.UserRole;
import com.coderiders.happyanimal.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long>{
    Optional<User> findById(Long id);
    Optional<User> findByLogin(String login);
    Page<User> getAllByNameContainsIgnoreCase(String name, Pageable pageable);
    Page<User> findAllByUserRole(UserRole userRole, Pageable pageable);
}
