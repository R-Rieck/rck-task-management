package com.rrieck.taskmanagementbackend.auth.config;

import com.rrieck.taskmanagementbackend.auth.model.accountMember.AccountMemberId;
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