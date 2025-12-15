package org.example.setlisttoplaylist.music.service;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.music.service.dto.PlaylistCreationResult;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.provider.MusicProvider;
import org.example.setlisttoplaylist.music.provider.MusicProviderFactory;
import org.example.setlisttoplaylist.scraper.dto.SetlistScrapResult;
import org.example.setlisttoplaylist.scraper.service.SetlistScraperService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlaylistCreationService {

    private final SetlistScraperService scraper;
    private final MusicProviderFactory providerFactory;

    public PlaylistCreationResult createPlaylistFromSetlist(String url, String role) {

        MusicProvider provider = providerFactory.getProvider(role);
        Provider providerEnum = Provider.fromRole(role);

        // 1. scrape tracks
        SetlistScrapResult scrap = scraper.scrapeTracks(url);

        // 2. search tracks
        List<Track> tracks = scrap.tracks().stream()
                .map(t -> provider.searchTrack(t.title(), scrap.artist()))
                .toList();

        // 3. create playlist
        String playlistId = provider.createPlaylist(scrap.artist() + " Setlist");

        // 4. add tracks
        List<String> providerTrackIds = tracks.stream()
                .map(Track::providerTrackId)
                .toList();

        provider.addTracksToPlaylist(playlistId, providerTrackIds);

        String playlistUrl = switch (providerEnum) {
            case SPOTIFY -> "https://open.spotify.com/playlist/" + playlistId;
            case YOUTUBE -> "https://www.youtube.com/playlist?list=" + playlistId;
        };

        // 5. return result
        return new PlaylistCreationResult(
                scrap.artist(),
                scrap.totalTracks(),
                playlistId,
                playlistUrl,
                providerEnum.key(),
                tracks
        );
    }
}
