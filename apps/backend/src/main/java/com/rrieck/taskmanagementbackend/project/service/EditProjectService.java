package com.rrieck.taskmanagementbackend.project.service;

import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.project.model.Project;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMember;
import com.rrieck.taskmanagementbackend.project.model.member.ProjectMemberId;
import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EditProjectService {
	private final ProjectRepository projectRepository;
	private final UserRepository userRepository;

	@Transactional
	public Project edit(ProjectId projectId, String newName, String description, String icon, List<UserId> memberIds) {
		Project existingProject = projectRepository.findById(projectId).orElseThrow();

		existingProject.setName(newName);
		existingProject.setDescription(description);
		existingProject.setIcon(icon);
		existingProject.setUpdatedAt(LocalDateTime.now());

		if (memberIds != null) {
			var members = existingProject.getMembers();
			var currentUserIds = members.stream()
				.map(m -> m.getUser().getId())
				.toList();

			var toRemove = members.stream()
				.filter(m -> !memberIds.contains(m.getUser().getId()))
				.toList();
			members.removeAll(toRemove);

			var toAdd = memberIds.stream()
				.filter(id -> !currentUserIds.contains(id))
				.toList();

			if (!toAdd.isEmpty()) {
				var userRefs = userRepository.getReferenceByIdIsIn(toAdd);
				for (var user : userRefs) {
					members.add(ProjectMember.builder()
						.id(ProjectMemberId.generateId())
						.project(existingProject)
						.user(user)
						.joinedAt(LocalDateTime.now())
						.build());
				}
			}
		}

		return projectRepository.save(existingProject);
	}
}
