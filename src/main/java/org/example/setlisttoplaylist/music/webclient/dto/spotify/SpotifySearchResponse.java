package org.example.setlisttoplaylist.music.webclient.dto.spotify;

import java.util.List;

public record SpotifySearchResponse(Tracks tracks) {
    public record Tracks(List<Item> items) {}
    public record Item(String id, String name, List<Artist> artists) {}
    public record Artist(String name) {}
}

//{
//    "tracks": {
//        "items": [
//            {
//                "id": "...",
//                "name": "Song Title",
//                "artists": [{ "name": "Artist Name" }]
//            }
//        ]
//    }
//}