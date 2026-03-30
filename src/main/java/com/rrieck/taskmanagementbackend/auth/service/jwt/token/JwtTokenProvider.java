package com.rrieck.taskmanagementbackend.auth.service.jwt.token;

import com.rrieck.taskmanagementbackend.account.model.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.jwt.JwtClaimKeys;
import com.rrieck.taskmanagementbackend.user.model.UserId;
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

	public Date extractExpiration(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return claims.getExpiration();
	}

	public String extractEmail(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return claims.getSubject();
	}

	public String extractType(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return claims.get(JwtClaimKeys.TYPE, String.class);
	}

	public UserId extractUserId(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return UserId.fromString(claims.get(JwtClaimKeys.USER_ID, String.class));
	}

	public AccountId extractAccountId(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return AccountId.fromString(claims.get(JwtClaimKeys.ACCOUNT_ID, String.class));
	}

	public Role extractRole(String token, String secret) {
		Claims claims = extractClaims(token, secret);
		return Role.valueOf(claims.get(JwtClaimKeys.ROLE, String.class));
	}

	Claims extractClaims(
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
