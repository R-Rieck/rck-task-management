package com.rrieck.taskmanagementbackend.auth.model.invitation;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "account_invitations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Invitation {
	@EmbeddedId
	private InvitationId id;

	@Column(nullable = false)
	private UUID invitationCode;

	@Column(nullable = false)
	private LocalDateTime expirationDate;

	@Column(nullable = false)
	private String inviteeEmail;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "invited_by_user", nullable = false))
	private UserId invitedByUser;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "invited_by_account", nullable = false))
	private AccountId invitedByAccount;
}
