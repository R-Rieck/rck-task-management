package com.rrieck.taskmanagementbackend.auth.repository;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, UserId> {
	Optional<User> findOptByEmail(String email);

	Optional<User> getOptById(UserId id);

	boolean existsByEmail(String email);

	List<User> getReferenceByIdIsIn(Collection<UserId> ids);
}
