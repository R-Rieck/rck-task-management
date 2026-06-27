package com.rrieck.taskmanagementbackend.board.config;

import com.rrieck.taskmanagementbackend.board.model.BoardId;
import com.rrieck.taskmanagementbackend.board.model.BoardSectionId;
import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class BoardGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer boardIdScalarConfigurer() {
		return builder -> {
			builder.scalar(IdentifierScalarFactory.identifierScalar("BoardId", BoardId::fromString));
			builder.scalar(IdentifierScalarFactory.identifierScalar("BoardSectionId", BoardSectionId::fromString));
		};
	}
}
