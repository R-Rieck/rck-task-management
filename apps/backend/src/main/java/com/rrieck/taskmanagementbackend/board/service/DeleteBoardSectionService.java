package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import com.rrieck.taskmanagementbackend.board.repository.BoardSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DeleteBoardSectionService {
	private final BoardSectionRepository boardSectionRepository;
	private final BoardRepository boardRepository;

	@Transactional
	public void delete(BoardSectionId sectionId) {
		var section = boardSectionRepository.findById(sectionId).orElseThrow();
		Board board = section.getBoard();
		board.getSections().remove(section);

		for (int i = 0; i < board.getSections().size(); i++) {
			board.getSections().get(i).setPosition(i);
		}

		board.setUpdatedAt(LocalDateTime.now());
		boardRepository.save(board);
	}
}
