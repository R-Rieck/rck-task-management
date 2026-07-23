package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardSection;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateBoardSectionService {
	private final BoardRepository boardRepository;

	@Transactional
	public BoardSection create(BoardId boardId, String name, Integer position) {
		Board board = boardRepository.findById(boardId).orElseThrow();
		var sections = board.getSections();

		int insertPosition = (position != null && position >= 0 && position <= sections.size())
			? position
			: sections.size();

		if (insertPosition <= sections.size()) {
			for (var section : sections) {
				if (section.getPosition() >= insertPosition) {
					section.setPosition(section.getPosition() + 1);
				}
			}
		}

		BoardSection section = BoardSection.builder()
			.id(BoardSectionId.generateId())
			.name(name)
			.position(insertPosition)
			.board(board)
			.build();

		sections.add(section);
		board.setUpdatedAt(LocalDateTime.now());
		boardRepository.save(board);

		return section;
	}
}
