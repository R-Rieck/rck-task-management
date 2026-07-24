package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBoardsService {
	private final BoardRepository boardRepository;
	private final BoardAuthorizationService boardAuthorizationService;

	@Transactional(readOnly = true)
	public List<Board> getBoards(ProjectId projectId, UserId userId) {
		return boardRepository.findByProjectId(projectId).stream()
			.filter(b -> boardAuthorizationService.canAccess(b, userId))
			.toList();
	}

	@Transactional(readOnly = true)
	public Board getBoard(BoardId boardId, UserId userId) {
		Board board = boardRepository.findById(boardId).orElseThrow();
		if (!boardAuthorizationService.canAccess(board, userId)) {
			throw new RuntimeException("Board not found");
		}
		return board;
	}
}
