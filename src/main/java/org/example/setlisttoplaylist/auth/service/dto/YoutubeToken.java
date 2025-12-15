package org.example.setlisttoplaylist.auth.service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record YoutubeToken(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("scope") String scope,
        @JsonProperty("token_type") String tokenType,
        @JsonProperty("expires_in") long expiresIn
) implements OAuthToken {}
