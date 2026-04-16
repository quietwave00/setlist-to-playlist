package org.example.setlisttoplaylist.music.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.music.service.dto.PlaylistCreationResult;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.provider.MusicProvider;
import org.example.setlisttoplaylist.music.provider.CurrentMusicProvider;
import org.example.setlisttoplaylist.scraper.dto.SetlistScrapResult;
import org.example.setlisttoplaylist.scraper.service.SetlistScraperService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PlaylistCreationService {

    private final SetlistScraperService scraper;
    private final CurrentMusicProvider currentMusicProvider;

    public PlaylistCreationResult createPlaylistFromSetlist(String url, Provider platform) {
        MusicProvider provider = currentMusicProvider.provider(platform);

        // 1. scrape tracks
        SetlistScrapResult scrap = scraper.scrapeTracks(url);
        log.debug("scrap: {}", scrap.toString());

        // 2. search tracks
        List<Track> tracks = scrap.tracks().stream()
                .map(t -> provider.searchTrack(t.title(), scrap.artist()))
                .toList();
        log.debug("tracks: {}", tracks.toString());


        // 3. create playlist
        String playlistId = provider.createPlaylist(scrap.setlistTitle());

        // 4. add tracks
        List<String> providerTrackIds = tracks.stream()
                .map(Track::providerTrackId)
                .toList();

        provider.addTracksToPlaylist(playlistId, providerTrackIds);

        String playlistUrl = switch (platform) {
            case SPOTIFY -> "https://open.spotify.com/playlist/" + playlistId;
            case YOUTUBE -> "https://www.youtube.com/playlist?list=" + playlistId;
        };

        // 5. return result
        return new PlaylistCreationResult(
                scrap.artist(),
                scrap.totalTracks(),
                playlistId,
                playlistUrl,
                platform.key(),
                tracks
        );
    }
}
