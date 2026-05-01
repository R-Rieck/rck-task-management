package com.rrieck.taskmanagementbackend.auth.config;

import com.rrieck.taskmanagementbackend.auth.model.invitation.InvitationId;
import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class InvitationGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer invitationIdScalarConfigurer() {
		return builder -> builder.scalar(
			IdentifierScalarFactory.identifierScalar("InvitationId", InvitationId::fromString)
		);
	}
}