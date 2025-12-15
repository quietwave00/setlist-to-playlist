package org.example.setlisttoplaylist.music.webclient.dto.youtube;

import java.util.List;

public record YoutubeSearchResponse(
        List<Item> items
) {
    public record Item(
            Id id,
            Snippet snippet
    ) {}

    public record Id(String videoId) {}

    public record Snippet(String title) {}
}

//{
//    "items": [
//        {
//        "id": { "videoId": "xxxx" },
//        "snippet": { "title": "video title" }
//        }
//    ]
//}