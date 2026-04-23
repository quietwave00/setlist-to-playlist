package org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SetlistFmSetlistResponse(
        String id,
        String eventDate,
        Artist artist,
        Venue venue,
        Sets sets,
        String url
) {
    public record Artist(
            String name
    ) {
    }

    public record Venue(
            String name,
            City city
    ) {
    }

    public record City(
            String name,
            Country country
    ) {
    }

    public record Country(
            String name,
            String code
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
