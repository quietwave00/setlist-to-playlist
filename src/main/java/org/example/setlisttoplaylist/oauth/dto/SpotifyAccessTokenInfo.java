package org.example.setlisttoplaylist.oauth.dto;

public record SpotifyAccessTokenInfo(
        String accessToken,
        long expiresIn
) {
}
