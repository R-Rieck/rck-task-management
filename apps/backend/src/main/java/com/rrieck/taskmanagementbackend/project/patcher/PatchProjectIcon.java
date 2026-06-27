package com.rrieck.taskmanagementbackend.project.patcher;

import com.rrieck.taskmanagementbackend.project.repository.ProjectRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PatchProjectIcon {
	private final ProjectRepository projectRepository;

	@PostConstruct
	public void setDefaultIcons() {
		projectRepository.findAll().stream()
			.filter(p -> p.getIcon() == null)
			.forEach(p -> {
				p.setIcon("FolderOutlined");
				projectRepository.save(p);
			});
	}
}
