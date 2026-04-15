package org.example.setlisttoplaylist.oauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "spotify.static")
@Data
public class SpotifyStaticProperties {
    private String clientId;
    private String clientSecret;
    private String refreshToken;
}

