package com.rrieck.taskmanagementbackend.board.repository;

import com.rrieck.taskmanagementbackend.board.model.Board;
import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, BoardId> {
	List<Board> findByProjectId(ProjectId projectId);
}
