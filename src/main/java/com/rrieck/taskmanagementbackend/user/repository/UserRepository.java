package com.rrieck.taskmanagementbackend.user.repository;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
	Optional<User> findOptByEmail(String email);

	boolean existsByEmail(String email);
}
