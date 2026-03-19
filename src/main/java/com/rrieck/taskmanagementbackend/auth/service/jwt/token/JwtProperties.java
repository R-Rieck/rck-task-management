package com.rrieck.taskmanagementbackend.auth.service.jwt.token;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
	private TokenConfig accessToken;
	private TokenConfig refreshToken;

	@Getter
	@Setter
	public static class TokenConfig {
		private String secret;
		private long expiration;
	}
}
