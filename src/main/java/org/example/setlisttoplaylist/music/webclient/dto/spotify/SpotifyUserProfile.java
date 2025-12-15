package org.example.setlisttoplaylist.music.webclient.dto.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SpotifyUserProfile(
        String id,
        @JsonProperty("display_name") String displayName,
        String email,
        String country,
        Followers followers,
        List<Image> images
) {
    public record Followers(int total) {}

    public record Image(
            String url,
            Integer height,
            Integer width
    ) {}
}
