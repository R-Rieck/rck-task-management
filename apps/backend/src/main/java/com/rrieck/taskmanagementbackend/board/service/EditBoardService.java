package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardMember;
import com.rrieck.taskmanagementbackend.board.model.BoardMemberId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EditBoardService {
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;
	private final BoardAuthorizationService boardAuthorizationService;

	@Transactional
	public Board edit(BoardId boardId, String newName, List<UserId> memberIds, UserId requesterId) {
		Board existingBoard = boardRepository.findById(boardId).orElseThrow();

		if (!boardAuthorizationService.canEditName(existingBoard, requesterId)) {
			throw new RuntimeException("Not authorized to edit this board");
		}

		existingBoard.setName(newName);
		existingBoard.setUpdatedAt(LocalDateTime.now());

		if (memberIds != null && boardAuthorizationService.canEditMembers(existingBoard, requesterId)) {
			List<UserId> toKeep = memberIds.stream()
				.filter(id -> !id.id().equals(existingBoard.getOwnerId().id()))
				.collect(Collectors.toList());

			existingBoard.getMembers().clear();

			if (!toKeep.isEmpty()) {
				List<User> userRefs = userRepository.getReferenceByIdIsIn(toKeep);
				List<BoardMember> newMembers = userRefs.stream().map(user -> BoardMember.builder()
					.id(BoardMemberId.generateId())
					.board(existingBoard)
					.user(user)
					.joinedAt(LocalDateTime.now())
					.build()).collect(Collectors.toList());
				existingBoard.getMembers().addAll(newMembers);
			}
		}

		return boardRepository.save(existingBoard);
	}
}
