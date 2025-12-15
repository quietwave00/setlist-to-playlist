package org.example.setlisttoplaylist.oauth;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.service.dto.YoutubeToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

@Component
@RequiredArgsConstructor
public class YoutubeOAuthClient {

    @Value("${youtube.client.id}")
    private String clientId;

    @Value("${youtube.client.secret}")
    private String clientSecret;

    @Value("${youtube.redirect.uri}")
    private String redirectUri;

    private static final String AUTH_BASE_URL = "https://accounts.google.com/o/oauth2/v2/auth";
    private static final String TOKEN_URL = "https://oauth2.googleapis.com/token";

    private static final String SCOPE = "https://www.googleapis.com/auth/youtube";

    private final WebClient webClient = WebClient.create();

    public String buildAuthUrl() {
        return UriComponentsBuilder
                .fromUriString(AUTH_BASE_URL)
                .queryParam("client_id", clientId)
                .queryParam("redirect_uri", redirectUri)
                .queryParam("response_type", "code")
                .queryParam("scope", SCOPE)
                .queryParam("access_type", "offline")
                .queryParam("include_granted_scopes", "true")
                .encode()
                .toUriString();
    }

    public YoutubeToken exchangeCodeForToken(String code) {
        return webClient.post()
                .uri(TOKEN_URL)
                .header(HttpHeaders.CONTENT_TYPE, "application/x-www-form-urlencoded")
                .body(BodyInserters.fromFormData("grant_type", "authorization_code")
                        .with("code", code)
                        .with("client_id", clientId)
                        .with("client_secret", clientSecret)
                        .with("redirect_uri", redirectUri)
                )
                .retrieve()
                .bodyToMono(YoutubeToken.class)
                .block();
    }
}
