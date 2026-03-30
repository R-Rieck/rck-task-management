package com.rrieck.taskmanagementbackend.accountMemeber.config;

import com.rrieck.taskmanagementbackend.accountMemeber.model.AccountMemberId;
import com.rrieck.taskmanagementbackend.common.graphql.IdentifierScalarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class AccountMemberGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer accountMemberIdScalarConfigurer() {
		return builder -> builder.scalar(
			IdentifierScalarFactory.identifierScalar("AccountMemberId", AccountMemberId::fromString)
		);
	}
}