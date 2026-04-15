package org.example.setlisttoplaylist.oauth;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.dto.SpotifyToken;
import org.example.setlisttoplaylist.oauth.config.SpotifyStaticProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
@RequiredArgsConstructor
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
                .bodyToMono(SpotifyToken.class)
                .block();
    }
}

