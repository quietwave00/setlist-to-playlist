package org.example.setlisttoplaylist.music.provider;

import org.example.setlisttoplaylist.music.domain.Track;

import java.util.List;

public interface MusicProvider {

    Track searchTrack(String title, String artist);

    String createPlaylist(String playlistName);

    void addTracksToPlaylist(String playlistId, List<String> providerTrackIds);
}

