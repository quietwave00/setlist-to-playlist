package org.example.setlisttoplaylist.music.service.dto;

import org.example.setlisttoplaylist.music.domain.Track;

import java.util.List;

public record PlaylistCreationResult(
        String artist,
        int totalTracks,
        String playlistId,
        String playlistUrl,
        String provider,
        List<Track> tracks
) {}
