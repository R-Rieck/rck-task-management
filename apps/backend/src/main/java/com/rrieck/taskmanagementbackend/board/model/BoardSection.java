package com.rrieck.taskmanagementbackend.board.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "board_sections")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BoardSection {
	@EmbeddedId
	private BoardSectionId id;

	@Column(nullable = false)
	private String name;

	@Column(nullable = false)
	private int position;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
	private Board board;
}
