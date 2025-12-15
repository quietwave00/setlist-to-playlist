package org.example.setlisttoplaylist.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifyToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn,
        @JsonProperty("refresh_token") String refreshToken,
        String scope
) implements OAuthToken {}

