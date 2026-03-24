package com.rrieck.taskmanagementbackend.user.repository;

import com.rrieck.taskmanagementbackend.user.model.User;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
	Optional<User> findOptByEmail(String email);

	boolean existsByEmail(String email);

	List<User> getReferenceByIdIsIn(Collection<UserId> ids);
}
