package org.example.setlisttoplaylist.scraper.dto;

import java.util.List;

public record SetlistScrapResult(
        String artist,
        List<SetlistTrack> tracks,
        int totalTracks,
        String setlistTitle
) {}

