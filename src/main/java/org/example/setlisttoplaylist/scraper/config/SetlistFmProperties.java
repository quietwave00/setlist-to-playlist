package org.example.setlisttoplaylist.scraper.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "setlistfm")
@Data
public class SetlistFmProperties {
    private String baseUrl = "https://api.setlist.fm/rest/1.0";
    private String apiKey;
}

