package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBoardService {
	private final BoardRepository boardRepository;
	private final BoardAuthorizationService boardAuthorizationService;

	public void delete(BoardId boardId, UserId requesterId) {
		Board board = boardRepository.findById(boardId).orElseThrow();
		if (!boardAuthorizationService.canDelete(board, requesterId)) {
			throw new RuntimeException("Not authorized to delete this board");
		}
		boardRepository.delete(board);
	}
}
