package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.board.repository.BoardSectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RenameBoardSectionService {
	private final BoardSectionRepository boardSectionRepository;

	@Transactional
	public void rename(BoardSectionId sectionId, String name) {
		var section = boardSectionRepository.findById(sectionId).orElseThrow();
		section.setName(name);
		boardSectionRepository.save(section);
	}
}
