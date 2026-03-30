package com.rrieck.taskmanagementbackend.invitation.model;

import com.rrieck.taskmanagementbackend.user.model.UserId;
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
	private String invitedEmail;

	@Column(nullable = false)
	private LocalDateTime expirationDate;

	@Embedded
	@AttributeOverride(name = "id", column = @Column(name = "invited_by", nullable = false))
	private UserId invitedBy;
}
