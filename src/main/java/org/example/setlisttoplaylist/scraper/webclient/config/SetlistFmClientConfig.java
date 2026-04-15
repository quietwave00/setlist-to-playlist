package org.example.setlisttoplaylist.scraper.webclient.config;

import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.scraper.config.SetlistFmProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@RequiredArgsConstructor
public class SetlistFmClientConfig {

    private final SetlistFmProperties properties;

    @Bean
    public WebClient setlistFmClient() {
        return WebClient.builder()
                .baseUrl(properties.getBaseUrl())
                .defaultHeader("x-api-key", properties.getApiKey())
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
                .build();
    }
}

