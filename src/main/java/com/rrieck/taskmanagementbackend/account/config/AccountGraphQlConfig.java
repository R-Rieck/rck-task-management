package com.rrieck.taskmanagementbackend.account.config;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
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