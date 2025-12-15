package org.example.setlisttoplaylist.music.service.dto;

import org.example.setlisttoplaylist.music.domain.Track;

import java.util.List;

public record PlaylistResult(
        String artist,
        int totalTracks,
        List<Track> items
) {}
