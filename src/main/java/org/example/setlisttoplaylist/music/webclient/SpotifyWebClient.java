package org.example.setlisttoplaylist.music.webclient;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.TokenService;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifyPlaylistResponse;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifySearchResponse;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifySnapshotResponse;
import org.example.setlisttoplaylist.music.webclient.dto.spotify.SpotifyUserProfile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class SpotifyWebClient {

    private final WebClient spotifyClient;
    private final TokenService tokenService;

    public SpotifySearchResponse search(String query) {
        String accessToken = tokenService.getAccessToken(Provider.SPOTIFY);

        return spotifyClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1/search")
                        .queryParam("q", query)
                        .queryParam("type", "track")
                        .queryParam("limit", 1)
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(SpotifySearchResponse.class)
                .block();
    }

    public String getCurrentUserId() {
        String token = tokenService.getAccessToken(Provider.SPOTIFY);
        SpotifyUserProfile profile = spotifyClient.get()
                .uri("/v1/me")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .retrieve()
                .onStatus(HttpStatusCode::isError, resp ->
                        Mono.error(new CustomException(ErrorCode.AUTHENTICATE_FAILED))
                )
                .bodyToMono(SpotifyUserProfile.class)
                .block();

        return profile.id();
    }

    public String createPlaylist(String userId, String playlistName) {
        String token = tokenService.getAccessToken(Provider.SPOTIFY);

        Map<String, Object> body = Map.of(
                "name", playlistName,
                "public", false,
                "description", playlistName
        );

        SpotifyPlaylistResponse response = spotifyClient.post()
                .uri("/v1/users/{userId}/playlists", userId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SpotifyPlaylistResponse.class)
                .block();

        return response.id();
    }

    public String addTracksToPlaylist(String playlistId, List<String> uris) {
        String token = tokenService.getAccessToken(Provider.SPOTIFY);

        Map<String, Object> body = Map.of(
                "uris", uris
        );

        SpotifySnapshotResponse response = spotifyClient.post()
                .uri("/v1/playlists/{playlistId}/tracks", playlistId)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(SpotifySnapshotResponse.class)
                .block();

        return response.snapshotId();
    }
}
