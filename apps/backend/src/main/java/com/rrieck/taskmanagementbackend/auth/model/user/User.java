package com.rrieck.taskmanagementbackend.auth.model.user;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
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

	@Column(nullable = false)
	private LocalDateTime registeredAt;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "last_used_account_id", nullable = false))
	private AccountId lastUsedAccountId;
}
