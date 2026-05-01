package com.rrieck.taskmanagementbackend.auth.repository;

import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {
	Optional<RefreshToken> findByToken(String token);

	Optional<RefreshToken> findByUserId(UserId id);

	void deleteByUserId(UserId id);
}
