package com.rrieck.taskmanagementbackend.auth;

import com.rrieck.taskmanagementbackend.auth.model.Role;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.AccountMemberRepository;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.repository.UserRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.LoginUserService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.RefreshAuthenticationService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.RegisterUserService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.accessToken.CheckAccessTokenForValidity;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtProperties;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AuthIntegrationTest {

    @Autowired private RegisterUserService registerUserService;
    @Autowired private LoginUserService loginUserService;
    @Autowired private RefreshAuthenticationService refreshAuthenticationService;
    @Autowired private UserRepository userRepository;
    @Autowired private RefreshTokenRepository refreshTokenRepository;
    @Autowired private AccountMemberRepository accountMemberRepository;
    @Autowired private JwtTokenProvider jwtTokenProvider;
    @Autowired private JwtProperties jwtProperties;
    @Autowired private CheckAccessTokenForValidity checkAccessTokenForValidity;

    private static final String TEST_EMAIL = "integration-test-" + UUID.randomUUID() + "@example.com";
    private static final String TEST_PASSWORD = "testPassword123!";
    private static final String TEST_NAME = "Integration Test User";

    private AuthTypes.AuthType registerResult;

    @BeforeEach
    void setUp() {
        registerResult = registerUserService.register(TEST_NAME, TEST_EMAIL, TEST_PASSWORD);
    }

    @Test
    void fullAuthCycle_registerThenLoginThenRefresh() {
        assertNotNull(registerResult);
        assertNotNull(registerResult.accessToken());
        assertNotNull(registerResult.refreshToken());
        assertNotNull(registerResult.userId());
        assertNotNull(registerResult.accountId());

        UserId userId = registerResult.userId();
        AccountId accountId = registerResult.accountId();

        assertTrue(userRepository.findOptByEmail(TEST_EMAIL).isPresent());
        assertTrue(accountMemberRepository.getOptByAccountIdAndUserId(accountId, userId).isPresent());
        assertEquals(Role.Admin, accountMemberRepository.getOptByAccountIdAndUserId(accountId, userId).get().getRole());

        AuthTypes.AuthType loginResult = loginUserService.login(TEST_EMAIL, TEST_PASSWORD);
        assertNotNull(loginResult);
        assertNotNull(loginResult.accessToken());
        assertNotNull(loginResult.refreshToken());
        assertEquals(userId.id(), loginResult.userId().id());
        assertEquals(accountId.id(), loginResult.accountId().id());

        String refreshToken = loginResult.refreshToken();
        AuthTypes.AuthType refreshResult = refreshAuthenticationService.refresh(refreshToken);
        assertNotNull(refreshResult);
        assertNotNull(refreshResult.accessToken());
        assertNotNull(refreshResult.refreshToken());
        assertEquals(userId.id(), refreshResult.userId().id());
        assertEquals(accountId.id(), refreshResult.accountId().id());

        assertNotNull(refreshResult.accessToken());
        assertNotNull(refreshResult.refreshToken());
        assertFalse(refreshResult.accessToken().isEmpty());
        assertFalse(refreshResult.refreshToken().isEmpty());

        assertTrue(
            refreshTokenRepository.findByToken(refreshResult.refreshToken()).isPresent()
        );
    }

    @Test
    void accessToken_shouldBeValidJwtAndContainCorrectClaims() {
        String secret = jwtProperties.getAccessToken().getSecret();
        String accessToken = registerResult.accessToken();
        String email = jwtTokenProvider.extractEmail(accessToken, secret);
        UserId userId = jwtTokenProvider.extractUserId(accessToken, secret);
        AccountId accountId = jwtTokenProvider.extractAccountId(accessToken, secret);

        assertEquals(TEST_EMAIL, email);
        assertEquals(registerResult.userId().id(), userId.id());
        assertEquals(registerResult.accountId().id(), accountId.id());
        assertTrue(checkAccessTokenForValidity.isValid(accessToken, TEST_EMAIL));
    }

    @Test
    void oldRefreshToken_shouldBeRevokedAfterRefresh() {
        String oldRefreshTokenStr = registerResult.refreshToken();

        refreshAuthenticationService.refresh(oldRefreshTokenStr);

        RefreshToken oldToken = refreshTokenRepository.findByToken(oldRefreshTokenStr).orElse(null);
        assertNotNull(oldToken);
        assertTrue(oldToken.isRevoked());
    }

    @Test
    void usedRefreshToken_shouldThrowRefreshTokenRevoked() {
        String oldRefreshTokenStr = registerResult.refreshToken();

        refreshAuthenticationService.refresh(oldRefreshTokenStr);

        assertThrows(
            com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenException.class,
            () -> refreshAuthenticationService.refresh(oldRefreshTokenStr)
        );
    }

    @Test
    void invalidRefreshToken_shouldThrowRefreshTokenNotFound() {
        assertThrows(
            com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenNotFound.class,
            () -> refreshAuthenticationService.refresh("nonexistent-token")
        );
    }
}
