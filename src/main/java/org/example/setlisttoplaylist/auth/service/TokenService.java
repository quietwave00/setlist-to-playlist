package org.example.setlisttoplaylist.auth.service;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.session.AuthSessionKeys;
import org.example.setlisttoplaylist.auth.service.dto.OAuthToken;
import org.example.setlisttoplaylist.global.exception.CustomException;
import org.example.setlisttoplaylist.global.exception.ErrorCode;
import org.springframework.stereotype.Service;

import java.util.EnumMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final HttpSession session;

    private static final String TOKEN_KEY = AuthSessionKeys.PROVIDER_TOKENS;

    public void saveExclusiveToken(Provider provider, OAuthToken token) {
        Map<Provider, OAuthToken> tokens = new EnumMap<>(Provider.class);
        tokens.put(provider, token);
        session.setAttribute(TOKEN_KEY, tokens);
    }

    public OAuthToken getToken(Provider provider) {
        Map<Provider, OAuthToken> tokens = getTokenMap();
        OAuthToken token = tokens.get(provider);
        if (token != null) {
            return token;
        }
        throw new CustomException(ErrorCode.TOKEN_EXPIRED);
    }

    public String getAccessToken(Provider provider) {
        return getToken(provider).accessToken();
    }

    public String getRefreshToken(Provider provider) {
        return getToken(provider).refreshToken();
    }

    public long expiresIn(Provider provider) {
        return getToken(provider).expiresIn();
    }

    public void clearAllTokens() {
        session.removeAttribute(TOKEN_KEY);
    }

    private Map<Provider, OAuthToken> getTokenMap() {
        Map<Provider, OAuthToken> tokens = (Map<Provider, OAuthToken>) session.getAttribute(TOKEN_KEY);
        if (tokens == null) {
            tokens = new EnumMap<>(Provider.class);
        }
        return tokens;
    }
}
