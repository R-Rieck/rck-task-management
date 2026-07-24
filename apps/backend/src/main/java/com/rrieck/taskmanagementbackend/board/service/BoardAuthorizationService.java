package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.board.model.Board;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BoardAuthorizationService {
	private final AccountMemberRepository accountMemberRepository;

	public boolean canAccess(Board board, UserId userId) {
		return isOwnerOrMember(board, userId);
	}

	public boolean canEditName(Board board, UserId userId) {
		return isOwnerOrMember(board, userId);
	}

	public boolean canEditMembers(Board board, UserId userId) {
		if (board.getOwnerId().id().equals(userId.id())) return true;
		boolean isMember = board.getMembers().stream()
			.anyMatch(m -> m.getUser().getId().id().equals(userId.id()));
		if (!isMember) return false;
		return accountMemberRepository
			.getOptByAccountIdAndUserId(board.getAccountId(), userId)
			.map(m -> m.getRole() == Role.Admin)
			.orElse(false);
	}

	public boolean canDelete(Board board, UserId userId) {
		return canEditMembers(board, userId);
	}

	private boolean isOwnerOrMember(Board board, UserId userId) {
		return board.getOwnerId().id().equals(userId.id())
			|| board.getMembers().stream()
				.anyMatch(m -> m.getUser().getId().id().equals(userId.id()));
	}
}
