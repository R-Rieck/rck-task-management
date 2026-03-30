package com.rrieck.taskmanagementbackend.user.config;

import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import com.rrieck.taskmanagementbackend.user.model.UserId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class UserGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer userIdScalarConfigurer() {
		return builder -> builder.scalar(
			IdentifierScalarFactory.identifierScalar("UserId", UserId::fromString)
		);
	}
}