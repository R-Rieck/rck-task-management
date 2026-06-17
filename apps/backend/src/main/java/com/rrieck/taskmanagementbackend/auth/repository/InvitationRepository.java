package com.rrieck.taskmanagementbackend.auth.repository;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.invitation.Invitation;
import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {
	public Optional<Invitation> getByInvitationCode(UUID invitationCode);

	public List<Invitation> getByInvitedByAccount(AccountId invitedByAccount);
}
