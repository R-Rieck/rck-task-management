package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeleteBoardService {
	private final BoardRepository boardRepository;

	public void delete(BoardId boardId) {
		boardRepository.deleteById(boardId);
	}
}
