package com.rrieck.taskmanagementbackend.invitation.repository;

import com.rrieck.taskmanagementbackend.invitation.model.Invitation;
import com.rrieck.taskmanagementbackend.invitation.model.InvitationId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvitationRepository extends JpaRepository<Invitation, InvitationId> {
}
