package org.example.setlisttoplaylist.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.service.dto.SessionStatus;
import org.example.setlisttoplaylist.auth.service.dto.SpotifyToken;
import org.example.setlisttoplaylist.auth.service.dto.YoutubeToken;
import org.example.setlisttoplaylist.auth.session.AuthSessionKeys;
import org.example.setlisttoplaylist.oauth.SpotifyOAuthClient;
import org.example.setlisttoplaylist.oauth.YoutubeOAuthClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final SpotifyOAuthClient spotifyOAuthClient;
    private final YoutubeOAuthClient youtubeOAuthClient;
    private final TokenService tokenService;

    public String authWithSpotify() {
        return spotifyOAuthClient.buildAuthUrl();
    }

    public void handleSpotifyCallback(String code, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        SpotifyToken token = spotifyOAuthClient.exchangeCodeForToken(code);
        tokenService.saveExclusiveToken(Provider.SPOTIFY, token);
        setActiveProvider(session, Provider.SPOTIFY);
    }

    public String authWithYoutube() {
        return youtubeOAuthClient.buildAuthUrl();
    }

    public void handleYoutubeCallback(String code, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        YoutubeToken token = youtubeOAuthClient.exchangeCodeForToken(code);
        tokenService.saveExclusiveToken(Provider.YOUTUBE, token);
        setActiveProvider(session, Provider.YOUTUBE);
    }

    public SessionStatus getSessionStatus(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            return SessionStatus.unauthenticated();
        }

        String activeProviderKey = (String) session.getAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        if (activeProviderKey == null) {
            return SessionStatus.unauthenticated();
        }

        Provider provider = Provider.fromKey(activeProviderKey);
        return SessionStatus.authenticated(provider);
    }

    public void logout(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        if (session != null) {
            tokenService.clearAllTokens();
            session.removeAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        }
    }

    private void setActiveProvider(HttpSession session, Provider provider) {
        // Clear any previous provider state implicitly by overwriting active provider.
        session.removeAttribute(AuthSessionKeys.PROVIDER_LOGIN_STATE);
        session.setAttribute(AuthSessionKeys.ACTIVE_PROVIDER, provider.key());
    }
}
