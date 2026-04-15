package org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SetlistFmSetlistResponse(
        String id,
        String eventDate,
        Artist artist,
        Sets sets,
        String url
) {
    public record Artist(
            String name
    ) {
    }

    public record Sets(
            @JsonProperty("set") List<Set> set
    ) {
    }

    public record Set(
            List<Song> song
    ) {
    }

    public record Song(
            String name
    ) {
    }
}

