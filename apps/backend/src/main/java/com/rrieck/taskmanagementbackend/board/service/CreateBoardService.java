package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardMember;
import com.rrieck.taskmanagementbackend.board.model.BoardMemberId;
import com.rrieck.taskmanagementbackend.board.model.BoardSection;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CreateBoardService {
	private final BoardRepository boardRepository;
	private final UserRepository userRepository;

	@Transactional
	public Board create(String name, List<String> sectionNames, ProjectId projectId, UserId ownerId, AccountId accountId, List<UserId> memberIds) {
		Board board = Board.builder()
			.id(BoardId.generateId())
			.name(name)
			.ownerId(ownerId)
			.accountId(accountId)
			.projectId(projectId)
			.createdAt(LocalDateTime.now())
			.updatedAt(LocalDateTime.now())
			.build();

		List<BoardSection> sections = sectionNames.stream()
			.map(sectionName -> BoardSection.builder()
				.id(BoardSectionId.generateId())
				.name(sectionName)
				.position(sectionNames.indexOf(sectionName))
				.board(board)
				.build())
			.toList();

		board.setSections(sections);

		if (memberIds != null && !memberIds.isEmpty()) {
			List<UserId> filtered = memberIds.stream()
				.filter(id -> !id.id().equals(ownerId.id()))
				.collect(Collectors.toList());
			if (!filtered.isEmpty()) {
				List<User> userRefs = userRepository.getReferenceByIdIsIn(filtered);
				List<BoardMember> members = userRefs.stream().map(user -> BoardMember.builder()
					.id(BoardMemberId.generateId())
					.board(board)
					.user(user)
					.joinedAt(LocalDateTime.now())
					.build()).collect(Collectors.toList());
				board.setMembers(members);
			}
		}

		return boardRepository.save(board);
	}
}
