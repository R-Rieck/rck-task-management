package com.rrieck.taskmanagementbackend.project.model;

import com.rrieck.taskmanagementbackend.user.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "project_members")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProjectMember {
	@Id
	@GeneratedValue(strategy = GenerationType.UUID)
	private UUID id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_project"))
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_user"))
	private User user;
}
