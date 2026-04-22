package org.example.setlisttoplaylist.scraper.service;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.exception.ApiException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.scraper.dto.SetlistSearchItem;
import org.example.setlisttoplaylist.scraper.dto.SetlistSearchResult;
import org.example.setlisttoplaylist.scraper.webclient.SetlistFmWebClient;
import org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm.SetlistFmSetlistsResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SetlistSearchService {

    private final SetlistFmWebClient webClient;

    public SetlistSearchResult search(
            String artistName,
            String cityName,
            String venueName,
            String year,
            Integer page
    ) {
        if (isBlank(artistName) && isBlank(cityName) && isBlank(venueName) && isBlank(year)) {
            throw new ApiException(ErrorCode.SETLIST_FM_INVALID_SEARCH_QUERY);
        }

        int normalizedPage = page == null || page < 1 ? 1 : page;
        SetlistFmSetlistsResponse response = webClient.searchSetlists(
                trimToNull(artistName),
                trimToNull(cityName),
                trimToNull(venueName),
                trimToNull(year),
                normalizedPage
        );

        List<SetlistSearchItem> items = response.setlists() == null
                ? List.of()
                : response.setlists().stream()
                .filter(Objects::nonNull)
                .map(this::toItem)
                .toList();

        return new SetlistSearchResult(
                items,
                defaultInt(response.total()),
                defaultInt(response.page()),
                defaultInt(response.itemsPerPage())
        );
    }

    private SetlistSearchItem toItem(SetlistFmSetlistsResponse.Setlist setlist) {
        SetlistFmSetlistsResponse.Venue venue = setlist.venue();
        SetlistFmSetlistsResponse.City city = venue == null ? null : venue.city();
        SetlistFmSetlistsResponse.Country country = city == null ? null : city.country();
        SetlistFmSetlistsResponse.Artist artist = setlist.artist();
        SetlistFmSetlistsResponse.Tour tour = setlist.tour();

        return new SetlistSearchItem(
                setlist.id(),
                setlist.url(),
                artist == null ? null : artist.name(),
                setlist.eventDate(),
                venue == null ? null : venue.name(),
                city == null ? null : city.name(),
                country == null ? null : country.name(),
                tour == null ? null : tour.name(),
                countSongs(setlist)
        );
    }

    private int countSongs(SetlistFmSetlistsResponse.Setlist setlist) {
        List<SetlistFmSetlistsResponse.Set> sets = setlist.sets() != null
                ? setlist.sets().set()
                : setlist.set();

        if (sets == null) {
            return 0;
        }

        return sets.stream()
                .filter(Objects::nonNull)
                .map(SetlistFmSetlistsResponse.Set::song)
                .filter(Objects::nonNull)
                .mapToInt(List::size)
                .sum();
    }

    private static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private static String trimToNull(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return value.trim();
    }

    private static int defaultInt(Integer value) {
        return value == null ? 0 : value;
    }
}
