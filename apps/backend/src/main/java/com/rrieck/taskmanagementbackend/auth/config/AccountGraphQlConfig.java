package com.rrieck.taskmanagementbackend.auth.config;

import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class AccountGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer accountIdScalarConfigurer() {
		return builder -> builder.scalar(
			IdentifierScalarFactory.identifierScalar("AccountId", AccountId::fromString)
		);
	}
}