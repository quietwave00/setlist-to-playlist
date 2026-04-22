package org.example.setlisttoplaylist.scraper.dto;

public record SetlistSearchItem(
        String id,
        String url,
        String artistName,
        String eventDate,
        String venueName,
        String cityName,
        String countryName,
        String tourName,
        int songCount
) {
}
