package org.example.setlisttoplaylist.music.webclient.dto.spotify;

import com.fasterxml.jackson.annotation.JsonProperty;

public record SpotifySnapshotResponse(
        @JsonProperty("snapshot_id") String snapshotId
)
{}