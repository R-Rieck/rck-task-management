package com.rrieck.taskmanagementbackend.board.model;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "board_members")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class BoardMember {
	@EmbeddedId
	private BoardMemberId id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "board_id", referencedColumnName = "id", nullable = false)
	private Board board;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
	private User user;

	@Column(nullable = false)
	private LocalDateTime joinedAt;
}
