package com.rrieck.taskmanagementbackend.user.model;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
	@EmbeddedId
	private UserId id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private String password;

	@Column(nullable = false)
	private String email;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Column(nullable = false)
	private LocalDateTime registeredAt;
}
