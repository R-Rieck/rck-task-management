package com.rrieck.taskmanagementbackend.common.config;

import com.rrieck.taskmanagementbackend.common.graphql.DateTimeScalarFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

@Configuration
public class DateTimeGraphQlConfig {
	@Bean
	RuntimeWiringConfigurer dateTimeScalarConfigurer() {
		return builder -> builder.scalar(DateTimeScalarFactory.dateTimeScalar());
	}
}