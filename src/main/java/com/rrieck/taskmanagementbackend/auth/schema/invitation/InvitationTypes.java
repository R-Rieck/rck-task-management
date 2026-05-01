package com.rrieck.taskmanagementbackend.auth.schema.invitation;

import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

public class InvitationTypes {
	@Builder
	public record InvitationType(
		InvitationId id,
		UUID InvitationToken,
		LocalDateTime expirationDate,
		String inviteeEmail,
		UserId invitedByUserId
	) {
		public static InvitationType from(Invitation invitation) {
			return InvitationType
				.builder()
				.id(invitation.getId())
				.InvitationToken(invitation.getInvitationCode())
				.expirationDate(invitation.getExpirationDate())
				.inviteeEmail(invitation.getInviteeEmail())
				.invitedByUserId(invitation.getInvitedByUser())
				.build();
		}
	}

}
