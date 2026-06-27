package com.rrieck.taskmanagementbackend.project.config;

import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import com.rrieck.taskmanagementbackend.project.model.ProjectId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class ProjectGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer projectIdScalarConfigurer() {
		return builder -> builder.scalar(
			IdentifierScalarFactory.identifierScalar("ProjectId", ProjectId::fromString)
		);
	}
}
