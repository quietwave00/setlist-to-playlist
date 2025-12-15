package org.example.setlisttoplaylist.music.webclient.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class YoutubeClientConfig {

    @Bean
    public WebClient youtubeClient() {
        return WebClient.builder()
                .baseUrl("https://youtube.googleapis.com")
                .build();
    }
}