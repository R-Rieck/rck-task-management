package com.rrieck.taskmanagementbackend.user.repository;

import com.rrieck.taskmanagementbackend.user.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findOptByEmail(String email);

    boolean existsByEmail(String email);
}
