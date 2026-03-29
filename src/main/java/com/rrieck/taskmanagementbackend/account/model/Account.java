package com.rrieck.taskmanagementbackend.account.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Account {
	@EmbeddedId
	private AccountId id;

	@Column(nullable = false)
	private String name;
}
