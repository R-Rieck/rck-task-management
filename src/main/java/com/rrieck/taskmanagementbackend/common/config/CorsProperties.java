package com.rrieck.taskmanagementbackend.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.validation.annotation.Validated;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

@Component
@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "app.cors")
public class CorsProperties {
	@NotEmpty
	private List<String> allowedOrigins;
	@NotEmpty
	private List<String> allowedMethods;
	@NotEmpty
	private List<String> allowedHeaders;
	private boolean allowCredentials;
}
