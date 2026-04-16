package org.example.setlisttoplaylist.music.provider;

import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.service.TokenService;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.springframework.stereotype.Component;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Component
public class CurrentMusicProvider {

    private final TokenService tokenService;
    private final Map<Provider, MusicProvider> providersByPlatform;

    public CurrentMusicProvider(TokenService tokenService, List<MusicProvider> providers) {
        this.tokenService = tokenService;
        this.providersByPlatform = indexProviders(providers);
    }

    public Provider activePlatform() {
        return tokenService.currentPlatform();
    }

    public MusicProvider provider() {
        Provider platform = activePlatform();
        return provider(platform);
    }

    public MusicProvider provider(Provider platform) {
        MusicProvider provider = providersByPlatform.get(platform);
        if (provider == null) {
            throw new CustomException(ErrorCode.INVALID_PROVIDER);
        }
        return provider;
    }

    private static Map<Provider, MusicProvider> indexProviders(List<MusicProvider> providers) {
        Map<Provider, MusicProvider> map = new EnumMap<>(Provider.class);
        for (MusicProvider provider : providers) {
            Provider platform = provider.platform();
            if (map.containsKey(platform)) {
                throw new IllegalStateException("Duplicate MusicProvider for platform: " + platform);
            }
            map.put(platform, provider);
        }
        return map;
    }
}
