package org.example.setlisttoplaylist.auth.service.dto;

import org.example.setlisttoplaylist.auth.domain.Provider;

public record SessionStatus(
        boolean authenticated,
        String activeProvider
) {
    public static SessionStatus unauthenticated() {
        return new SessionStatus(false, null);
    }

    public static SessionStatus authenticated(Provider provider) {
        return new SessionStatus(true, provider.key());
    }
}
