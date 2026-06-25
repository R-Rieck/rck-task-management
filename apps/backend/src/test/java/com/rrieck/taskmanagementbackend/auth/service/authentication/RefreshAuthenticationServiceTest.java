package com.rrieck.taskmanagementbackend.auth.service.authentication;

import com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenExpired;
import com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenInvalid;
import com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenNotFound;
import com.rrieck.taskmanagementbackend.auth.exception.refreshToken.RefreshTokenRevoked;
import com.rrieck.taskmanagementbackend.auth.model.account.AccountId;
import com.rrieck.taskmanagementbackend.auth.model.jwt.RefreshToken;
import com.rrieck.taskmanagementbackend.auth.model.jwt.TokenPair;
import com.rrieck.taskmanagementbackend.auth.model.user.User;
import com.rrieck.taskmanagementbackend.auth.model.user.UserId;
import com.rrieck.taskmanagementbackend.auth.repository.RefreshTokenRepository;
import com.rrieck.taskmanagementbackend.auth.schema.authentication.AuthTypes;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.IssueRefreshTokenPairService;
import com.rrieck.taskmanagementbackend.auth.service.authentication.jwt.refreshToken.CheckRefreshTokenForValidity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RefreshAuthenticationServiceTest {

    @Mock private IssueRefreshTokenPairService issueRefreshTokenPairService;
    @Mock private RefreshTokenRepository refreshTokenRepository;
    @Mock private CheckRefreshTokenForValidity checkRefreshTokenForValidity;
    @Mock private RefreshToken existingRefreshToken;
    @Mock private User user;

    private RefreshAuthenticationService service;
    private final String rawToken = "some-refresh-token-jwt";
    private final UserId userId = UserId.fromString(UUID.randomUUID().toString());
    private final AccountId accountId = AccountId.fromString(UUID.randomUUID().toString());
    private final TokenPair newTokenPair = TokenPair.builder()
        .accessToken("new-access-token")
        .refreshToken("new-refresh-token")
        .build();

    @BeforeEach
    void setUp() {
        service = new RefreshAuthenticationService(
            issueRefreshTokenPairService,
            refreshTokenRepository,
            checkRefreshTokenForValidity
        );
    }

    @Test
    void refresh_shouldReturnNewAuthTypeWhenTokenIsValid() {
        when(refreshTokenRepository.findByToken(rawToken)).thenReturn(Optional.of(existingRefreshToken));
        when(existingRefreshToken.getUser()).thenReturn(user);
        when(existingRefreshToken.isRevoked()).thenReturn(false);
        when(existingRefreshToken.getExpiration()).thenReturn(LocalDateTime.now().plusDays(1));
        when(user.getId()).thenReturn(userId);
        when(user.getLastUsedAccountId()).thenReturn(accountId);
        when(checkRefreshTokenForValidity.isValid(rawToken, userId.id().toString())).thenReturn(true);
        when(issueRefreshTokenPairService.issue(userId, existingRefreshToken)).thenReturn(newTokenPair);

        AuthTypes.AuthType result = service.refresh(rawToken);

        assertNotNull(result);
        assertEquals("new-access-token", result.accessToken());
        assertEquals("new-refresh-token", result.refreshToken());
        assertEquals(userId, result.userId());
        assertEquals(accountId, result.accountId());

        verify(issueRefreshTokenPairService).issue(userId, existingRefreshToken);
    }

    @Test
    void refresh_shouldThrowRefreshTokenNotFoundWhenTokenMissingFromDb() {
        when(refreshTokenRepository.findByToken(rawToken)).thenReturn(Optional.empty());

        assertThrows(RefreshTokenNotFound.class, () -> service.refresh(rawToken));
        verifyNoInteractions(issueRefreshTokenPairService);
    }

    @Test
    void refresh_shouldThrowRefreshTokenRevokedWhenTokenIsRevoked() {
        when(refreshTokenRepository.findByToken(rawToken)).thenReturn(Optional.of(existingRefreshToken));
        when(existingRefreshToken.getUser()).thenReturn(user);
        when(existingRefreshToken.isRevoked()).thenReturn(true);

        assertThrows(RefreshTokenRevoked.class, () -> service.refresh(rawToken));
        verifyNoInteractions(issueRefreshTokenPairService);
        verifyNoInteractions(checkRefreshTokenForValidity);
    }

    @Test
    void refresh_shouldThrowRefreshTokenExpiredWhenTokenIsExpired() {
        when(refreshTokenRepository.findByToken(rawToken)).thenReturn(Optional.of(existingRefreshToken));
        when(existingRefreshToken.getUser()).thenReturn(user);
        when(existingRefreshToken.isRevoked()).thenReturn(false);
        when(existingRefreshToken.getExpiration()).thenReturn(LocalDateTime.now().minusDays(1));

        assertThrows(RefreshTokenExpired.class, () -> service.refresh(rawToken));
        verifyNoInteractions(issueRefreshTokenPairService);
        verifyNoInteractions(checkRefreshTokenForValidity);
    }

    @Test
    void refresh_shouldThrowRefreshTokenInvalidWhenJwtValidationFails() {
        when(refreshTokenRepository.findByToken(rawToken)).thenReturn(Optional.of(existingRefreshToken));
        when(existingRefreshToken.getUser()).thenReturn(user);
        when(existingRefreshToken.isRevoked()).thenReturn(false);
        when(existingRefreshToken.getExpiration()).thenReturn(LocalDateTime.now().plusDays(1));
        when(user.getId()).thenReturn(userId);
        when(checkRefreshTokenForValidity.isValid(rawToken, userId.id().toString())).thenReturn(false);

        assertThrows(RefreshTokenInvalid.class, () -> service.refresh(rawToken));
        verifyNoInteractions(issueRefreshTokenPairService);
    }
}
