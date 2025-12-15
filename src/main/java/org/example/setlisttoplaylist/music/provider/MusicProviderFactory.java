package org.example.setlisttoplaylist.music.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MusicProviderFactory {

    private final SpotifyMusicProvider spotify;
    private final YouTubeMusicProvider youtube;

    public MusicProvider getProvider(String role) {
        return switch (role) {
            case "ROLE_SPOTIFY" -> spotify;
            case "ROLE_YOUTUBE" -> youtube;
            default -> throw new IllegalArgumentException("Unknown provider role: " + role);
        };
    }
}
