package org.example.setlisttoplaylist.music.provider;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.webclient.SpotifyWebClient;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifySearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SpotifyMusicProvider implements MusicProvider {

    private final SpotifyWebClient client;

    @Override
    public Track searchTrack(String title, String artist) {
        SpotifySearchResponse res = client.search(artist + " " + title);
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
        return client.createPlaylist(userId, playlistName);
    }

    @Override
    public void addTracksToPlaylist(String playlistId, List<String> trackIds) {
        List<String> uris = trackIds.stream()
                .map(id -> "spotify:track:" + id)
                .toList();
        client.addTracksToPlaylist(playlistId, uris);
    }
}
