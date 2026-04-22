package org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record SetlistFmSetlistsResponse(
        @JsonProperty("setlist") List<Setlist> setlists,
        Integer total,
        Integer page,
        Integer itemsPerPage
) {
    public record Setlist(
            String id,
            String url,
            String eventDate,
            Artist artist,
            Venue venue,
            Tour tour,
            Sets sets,
            @JsonProperty("set") List<Set> set
    ) {
    }

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

    public record Tour(
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
