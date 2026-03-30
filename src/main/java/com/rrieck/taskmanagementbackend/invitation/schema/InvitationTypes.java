package com.rrieck.taskmanagementbackend.invitation.schema;

import lombok.Builder;

public class InvitationTypes {
	@Builder
	public record InvitationType(
		Integer invitesSent
	) {
	}
}
