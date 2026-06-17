package com.rrieck.taskmanagementbackend.auth.model.jwt;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenPair {
    String accessToken;
    String refreshToken;
}
