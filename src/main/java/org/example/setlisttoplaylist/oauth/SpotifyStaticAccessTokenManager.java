package org.example.setlisttoplaylist.oauth;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.oauth.config.SpotifyStaticProperties;
import org.example.setlisttoplaylist.oauth.dto.SpotifyAccessTokenInfo;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;

@Component
@RequiredArgsConstructor
public class SpotifyStaticAccessTokenManager {

    private static final Duration REFRESH_SAFETY_WINDOW = Duration.ofMinutes(1);

    private final SpotifyStaticProperties properties;
    private final SpotifyTokenClient tokenClient;

    private volatile String cachedAccessToken;
    private volatile Instant expiresAt;

    public String getAccessToken() {
        return getAccessTokenInfo().accessToken();
    }

    public SpotifyAccessTokenInfo getAccessTokenInfo() {
        ensureConfigured();
        if (isValid()) {
            return new SpotifyAccessTokenInfo(cachedAccessToken, secondsUntilExpiry());
        }

        synchronized (this) {
            if (isValid()) {
                return new SpotifyAccessTokenInfo(cachedAccessToken, secondsUntilExpiry());
            }

            var token = tokenClient.refreshAccessToken();
            if (token == null || token.accessToken() == null || token.accessToken().isBlank()) {
                throw new CustomException(ErrorCode.AUTHENTICATE_FAILED);
            }

            cachedAccessToken = token.accessToken();
            expiresAt = Instant.now().plusSeconds(token.expiresIn());
            return new SpotifyAccessTokenInfo(cachedAccessToken, secondsUntilExpiry());
        }
    }

    private boolean isValid() {
        if (cachedAccessToken == null || cachedAccessToken.isBlank() || expiresAt == null) {
            return false;
        }
        return Instant.now().isBefore(expiresAt.minus(REFRESH_SAFETY_WINDOW));
    }

    private long secondsUntilExpiry() {
        if (expiresAt == null) {
            return 0;
        }
        long seconds = Duration.between(Instant.now(), expiresAt).getSeconds();
        return Math.max(seconds, 0);
    }

    private void ensureConfigured() {
        if (properties.getClientId() == null || properties.getClientId().isBlank()
                || properties.getClientSecret() == null || properties.getClientSecret().isBlank()
                || properties.getRefreshToken() == null || properties.getRefreshToken().isBlank()) {
            throw new CustomException(ErrorCode.AUTHENTICATE_FAILED);
        }
    }
}
