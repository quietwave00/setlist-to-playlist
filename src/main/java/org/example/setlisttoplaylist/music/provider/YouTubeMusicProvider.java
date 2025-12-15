package org.example.setlisttoplaylist.music.provider;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.music.domain.Track;
import org.example.setlisttoplaylist.music.webclient.YoutubeWebClient;
import org.example.setlisttoplaylist.music.webclient.dto.youtube.YoutubeSearchResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class YouTubeMusicProvider implements MusicProvider {

    private final YoutubeWebClient client;

    @Override
    public Track searchTrack(String title, String artist) {
        YoutubeSearchResponse res = client.search(title + " " + artist);
        var item = res.items().get(0);

        return new Track(
                item.snippet().title(),
                artist,
                item.id().videoId(),
                "https://www.youtube.com/watch?v=" + item.id().videoId()
        );
    }

    @Override
    public String createPlaylist(String playlistName) {
        return client.createPlaylist(playlistName);
    }

    @Override
    public void addTracksToPlaylist(String playlistId, List<String> videoIds) {
        videoIds.forEach(videoId -> {
                    client.addTrackToPlaylist(playlistId, videoId);
                });
    }
}
