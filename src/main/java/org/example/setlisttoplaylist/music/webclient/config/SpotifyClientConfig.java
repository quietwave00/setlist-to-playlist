package org.example.setlisttoplaylist.music.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class SpotifyClientConfig {

    @Bean
    public WebClient spotifyClient() {
        return WebClient.builder()
                .baseUrl("https://api.spotify.com")
                .build();
    }
}
