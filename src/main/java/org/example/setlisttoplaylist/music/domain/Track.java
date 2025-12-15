package org.example.setlisttoplaylist.music.domain;

public record Track(
        String title,
        String artist,
        String providerTrackId,// Spotify: trackId, YouTube: videoId
        String providerLink
) {}
