package com.rrieck.taskmanagementbackend.auth.service.jwt.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
public class JwtTokenProvider {
	public String generateToken(
		String subject,
		String secret,
		long expirationMs,
		Map<String, Object> claims
	) {
		Map<String, Object> finalClaims = new HashMap<>(claims);

		return Jwts.builder()
		           .claims(finalClaims)
		           .subject(subject)
		           .issuedAt(new Date())
		           .expiration(new Date(System.currentTimeMillis() + expirationMs))
		           .signWith(getSigningKey(secret))
		           .compact();
	}

	public Claims extractClaims(
		String token,
		String secret
	) {
		return Jwts.parser()
		           .verifyWith(getSigningKey(secret))
		           .build()
		           .parseSignedClaims(token)
		           .getPayload();
	}

	private SecretKey getSigningKey(String secret) {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
	}
}
