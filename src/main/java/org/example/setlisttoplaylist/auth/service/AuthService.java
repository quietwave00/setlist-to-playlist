package org.example.setlisttoplaylist.auth.service;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.auth.domain.Provider;
import org.example.setlisttoplaylist.auth.service.dto.SessionStatus;
import org.example.setlisttoplaylist.auth.service.dto.YoutubeToken;
import org.example.setlisttoplaylist.auth.session.AuthSessionKeys;
import org.example.setlisttoplaylist.oauth.YoutubeOAuthClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final YoutubeOAuthClient youtubeOAuthClient;
    private final TokenService tokenService;

    public String authWithYoutube() {
        return youtubeOAuthClient.buildAuthUrl();
    }

    public void handleYoutubeCallback(String code, HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.removeAttribute(AuthSessionKeys.ACTIVE_PROVIDER);
        session.removeAttribute(AuthSessionKeys.ACTIVE_TOKEN);
        org.springframework.security.core.context.SecurityContextHolder.clearContext();

        YoutubeToken token = youtubeOAuthClient.exchangeCodeForToken(code);
        session.setAttribute(AuthSessionKeys.ACTIVE_PROVIDER, Provider.YOUTUBE.key());
        session.setAttribute(AuthSessionKeys.ACTIVE_TOKEN, token);
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
            tokenService.clear();
            session.invalidate(); // fully clear session to avoid stale auth/tokens
        }
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
    }
}
