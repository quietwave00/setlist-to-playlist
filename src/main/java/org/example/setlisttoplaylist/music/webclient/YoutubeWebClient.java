package org.example.setlisttoplaylist.music.webclient;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.service.TokenService;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.music.webclient.dto.youtube.YoutubePlaylistCreateResponse;
import org.example.setlisttoplaylist.music.webclient.dto.youtube.YoutubePlaylistItemInsertResponse;
import org.example.setlisttoplaylist.music.webclient.dto.youtube.YoutubeSearchResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class YoutubeWebClient {

    private final WebClient youtubeClient;
    private final TokenService tokenService;

    public YoutubeSearchResponse search(String query) {
        String accessToken = tokenService.getAccessToken(Provider.YOUTUBE);
        if (accessToken == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }

        return youtubeClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/youtube/v3/search")
                        .queryParam("part", "snippet")
                        .queryParam("q", query)
                        .queryParam("type", "video")
                        .queryParam("maxResults", 1)
                        .build()
                )
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(YoutubeSearchResponse.class)
                .block();
    }

    public String createPlaylist(String playlistName) {
        String accessToken = tokenService.getAccessToken(Provider.YOUTUBE);

        Map<String, Object> body = Map.of(
                "snippet", Map.of(
                        "title", playlistName,
                        "description", playlistName
                ),
                "status", Map.of(
                        "privacyStatus", "private"
                )
        );

        YoutubePlaylistCreateResponse res = youtubeClient.post()
                .uri("/youtube/v3/playlists?part=snippet,status")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(YoutubePlaylistCreateResponse.class)
                .block();

        return res.id();
    }

    public void addTrackToPlaylist(String playlistId, String videoId) {
        String accessToken = tokenService.getAccessToken(Provider.YOUTUBE);

        Map<String, Object> body = Map.of(
                "snippet", Map.of(
                        "playlistId", playlistId,
                        "resourceId", Map.of(
                                "kind", "youtube#video",
                                "videoId", videoId
                        )
                )
        );

        try {
            youtubeClient.post()
                    .uri("/youtube/v3/playlistItems?part=snippet")
                    .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(body)
                    .retrieve()
                    .bodyToMono(YoutubePlaylistItemInsertResponse.class)
                    .block();

        } catch (WebClientResponseException.Conflict e) {
            log.info("YouTube video already exists in playlist. videoId={}", videoId);
        } catch (WebClientResponseException e) {
            log.error("YouTube API error while adding video. status={}, body={}",
                    e.getStatusCode(), e.getResponseBodyAsString());
            throw e;
        }
    }

}
