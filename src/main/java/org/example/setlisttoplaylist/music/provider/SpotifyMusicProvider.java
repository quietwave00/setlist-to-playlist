package org.example.setlisttoplaylist.music.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.webclient.SpotifyWebClient;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifySearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpotifyMusicProvider implements MusicProvider {

    private final SpotifyWebClient client;

    @Override
    public org.example.setlisttoplaylist.auth.domain.Provider platform() {
        return org.example.setlisttoplaylist.auth.domain.Provider.SPOTIFY;
    }

    @Override
    public Track searchTrack(String title, String artist) {
//        SpotifySearchResponse res = client.search(artist + " " + title);
//        var item = res.tracks().items().get(0);
//
        SpotifySearchResponse res = client.search(artist + " " + title);

        int size = res.tracks() == null || res.tracks().items() == null
                ? -1
                : res.tracks().items().size();

        log.debug(
                "Spotify search query='{}', items={}",
                artist + " " + title,
                size
        );

        if (size <= 0) {
            throw new IllegalStateException(
                    "Spotify search returned no items for query=" + artist + " " + title
            );
        }

        var item = res.tracks().items().get(0);

        return new Track(
                item.name(),
                item.artists().get(0).name(),
                item.id(),
                "https://open.spotify.com/track/" + item.id()
        );
    }

    @Override
    public String createPlaylist(String playlistName) {
        String userId = client.getCurrentUserId();
        log.debug("Current user id: {}", userId);
        return client.createPlaylist(userId, playlistName);
    }

    @Override
    public void addTracksToPlaylist(String playlistId, List<String> trackIds) {
        log.debug("Adding tracks to playlist: {}", trackIds.toString());
        List<String> uris = trackIds.stream()
                .map(id -> "spotify:track:" + id)
                .toList();
        client.addTracksToPlaylist(playlistId, uris);
    }
}
