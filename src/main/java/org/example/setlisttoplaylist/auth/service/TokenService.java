package org.example.setlisttoplaylist.auth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.session.AuthSessionKeys;
import org.example.setlisttoplaylist.auth.service.dto.OAuthToken;
import org.example.setlisttoplaylist.auth.service.dto.YoutubeToken;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.example.setlisttoplaylist.oauth.SpotifyStaticAccessTokenManager;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final HttpSession session;
    private final SpotifyStaticAccessTokenManager spotifyTokenManager;

    private final Map<Provider, Supplier<String>> accessTokenSuppliers = new EnumMap<>(Provider.class);

    @jakarta.annotation.PostConstruct
    void init() {
        accessTokenSuppliers.put(Provider.SPOTIFY, spotifyTokenManager::getAccessToken);
        accessTokenSuppliers.put(Provider.YOUTUBE, this::getYoutubeAccessToken);
    }

    public void activateYoutube(YoutubeToken token) {
        session.setAttribute(AuthSessionKeys.ACTIVE_PROVIDER, Provider.YOUTUBE.key());
        session.setAttribute(AuthSessionKeys.ACTIVE_TOKEN, token);
    }

    public Provider currentPlatform() {
        String activeProviderKey = (String) session.getAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        if (activeProviderKey == null || activeProviderKey.isBlank()) {
            return Provider.SPOTIFY;
        }
        return Provider.fromKey(activeProviderKey);
    }

    public String getAccessToken(Provider provider) {
        Supplier<String> supplier = accessTokenSuppliers.get(provider);
        if (supplier == null) {
            throw new CustomException(ErrorCode.UNKNOWN_ERROR);
        }
        return supplier.get();
    }

    private String getYoutubeAccessToken() {
        OAuthToken token = (OAuthToken) session.getAttribute(AuthSessionKeys.ACTIVE_TOKEN);
        String activeProviderKey = (String) session.getAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        if (token == null || activeProviderKey == null || activeProviderKey.isBlank()) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        Provider active = Provider.fromKey(activeProviderKey);
        if (active != Provider.YOUTUBE) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        return token.accessToken();
    }

    public String getRefreshToken() {
        OAuthToken token = (OAuthToken) session.getAttribute(AuthSessionKeys.ACTIVE_TOKEN);
        if (token == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        return token.refreshToken();
    }

    public long expiresIn() {
        OAuthToken token = (OAuthToken) session.getAttribute(AuthSessionKeys.ACTIVE_TOKEN);
        if (token == null) {
            throw new CustomException(ErrorCode.TOKEN_EXPIRED);
        }
        return token.expiresIn();
    }

    public void clear() {
        session.removeAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        session.removeAttribute(AuthSessionKeys.ACTIVE_TOKEN);
    }
}
