package com.rrieck.taskmanagementbackend.project.model.member;

import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.project.model.Project;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "project_members")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProjectMember {
	@EmbeddedId
	private ProjectMemberId id;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "project_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_project"))
	private Project project;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false, foreignKey = @ForeignKey(name = "fk_project_member_user"))
	private User user;

	@Column(nullable = false)
	private LocalDateTime joinedAt;
}
