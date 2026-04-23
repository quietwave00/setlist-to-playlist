package org.example.setlisttoplaylist.scraper.service;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.exception.ApiException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.scraper.dto.SetlistScrapResult;
import org.example.setlisttoplaylist.scraper.dto.SetlistTrack;
import org.example.setlisttoplaylist.scraper.webclient.SetlistFmWebClient;
import org.example.setlisttoplaylist.scraper.webclient.dto.setlistfm.SetlistFmSetlistResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SetlistFmSetlistService implements SetlistScraperService {

    private final SetlistFmWebClient webClient;

    @Override
    public SetlistScrapResult scrapeTracks(String setlistUrl) {
        String setlistId;
        try {
            setlistId = SetlistFmUrlParser.extractSetlistId(setlistUrl);
        } catch (RuntimeException e) {
            throw new ApiException(ErrorCode.SETLIST_FM_INVALID_URL);
        }

        SetlistFmSetlistResponse response = webClient.getSetlist(setlistId);

        String artist = response.artist() != null ? response.artist().name() : null;
        if (artist == null || artist.isBlank()) {
            throw new ApiException(ErrorCode.SETLIST_FM_UPSTREAM_ERROR);
        }

        List<SetlistTrack> tracks = extractTracks(response);
        if (tracks.isEmpty()) {
            throw new ApiException(ErrorCode.SETLIST_FM_EMPTY_SONG_LIST);
        }

        String title = deriveSetlistTitle(artist, response.eventDate(), extractCountryName(response));
        return new SetlistScrapResult(artist, tracks, tracks.size(), title);
    }

    private List<SetlistTrack> extractTracks(SetlistFmSetlistResponse response) {
        if (response.sets() == null || response.sets().set() == null) {
            return List.of();
        }

        return response.sets().set().stream()
                .filter(Objects::nonNull)
                .flatMap(set -> set.song() == null ? List.<SetlistFmSetlistResponse.Song>of().stream() : set.song().stream())
                .filter(Objects::nonNull)
                .map(SetlistFmSetlistResponse.Song::name)
                .filter(Objects::nonNull)
                .map(String::trim)
                .filter(name -> !name.isBlank())
                .map(SetlistTrack::new)
                .toList();
    }

    private String extractCountryName(SetlistFmSetlistResponse response) {
        if (response.venue() == null
                || response.venue().city() == null
                || response.venue().city().country() == null) {
            return null;
        }
        return response.venue().city().country().name();
    }

    private String deriveSetlistTitle(String artist, String eventDate, String countryName) {
        String suffix = countryName == null || countryName.isBlank()
                ? ""
                : " - " + countryName.trim();

        if (eventDate == null || eventDate.isBlank()) {
            return artist + " Setlist" + suffix;
        }
        return artist + " - " + eventDate + suffix;
    }
}
