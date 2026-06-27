package com.rrieck.taskmanagementbackend.board.service;

import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.repository.BoardRepository;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetBoardsService {
	private final BoardRepository boardRepository;

	public List<Board> getBoards(ProjectId projectId) {
		return boardRepository.findByProjectId(projectId);
	}
}
