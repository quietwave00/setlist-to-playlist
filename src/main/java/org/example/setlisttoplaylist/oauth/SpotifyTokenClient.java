package org.example.setlisttoplaylist.oauth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.auth.service.dto.SpotifyToken;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.oauth.config.SpotifyStaticProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
@Slf4j
public class SpotifyTokenClient {

    private final SpotifyStaticProperties properties;
    private final WebClient webClient = WebClient.create();

    public SpotifyToken refreshAccessToken() {
        String basic = Base64.getEncoder()
                .encodeToString((properties.getClientId() + ":" + properties.getClientSecret())
                        .getBytes(StandardCharsets.UTF_8));

        return webClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "refresh_token")
                        .with("refresh_token", properties.getRefreshToken())
                )
                .retrieve()
                .onStatus(HttpStatusCode::isError, response ->
                        response.bodyToMono(String.class)
                                .defaultIfEmpty("")
                                .flatMap(body -> {
                                    log.warn("Spotify token refresh failed. status={}, body={}",
                                            response.statusCode(), body);
                                    return Mono.error(new CustomException(ErrorCode.AUTHENTICATE_FAILED));
                                })
                )
                .bodyToMono(SpotifyToken.class)
                .block();
    }
}
