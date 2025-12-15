package org.example.setlisttoplaylist.oauth;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.dto.SpotifyToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Base64;

@Component
@RequiredArgsConstructor
public class SpotifyOAuthClient {

    @Value("${spotify.client.id}")
    private String clientId;

    @Value("${spotify.client.secret}")
    private String clientSecret;

    @Value("${spotify.redirect.uri}")
    private String redirectUri;

    private static final String SCOPE =
            "playlist-modify-public playlist-modify-private user-read-email";

    private final WebClient webClient = WebClient.create();

    public String buildAuthUrl() {
        return UriComponentsBuilder
                .fromUriString("https://accounts.spotify.com/authorize")
                .queryParam("client_id", clientId)
                .queryParam("response_type", "code")
                .queryParam("redirect_uri", redirectUri)
                .queryParam("scope", SCOPE)
                .encode()
                .toUriString();
    }

    public SpotifyToken exchangeCodeForToken(String code) {
        String basic = Base64.getEncoder()
                .encodeToString((clientId + ":" + clientSecret).getBytes());

        return webClient.post()
                .uri("https://accounts.spotify.com/api/token")
                .header(HttpHeaders.AUTHORIZATION, "Basic " + basic)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("code", code)
                        .with("redirect_uri", redirectUri)
                )
                .retrieve()
                .bodyToMono(SpotifyToken.class)
                .block();
    }
}
