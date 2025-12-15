package org.example.setlisttoplaylist.auth.service.dto;

import org.example.setlisttoplaylist.auth.domain.Provider;

import java.util.Map;

public record ProviderLoginStatus(
        boolean spotify,
        boolean youtube
) {
    public static ProviderLoginStatus fromState(Map<Provider, Boolean> state) {
        return new ProviderLoginStatus(
                Boolean.TRUE.equals(state.get(Provider.SPOTIFY)),
                Boolean.TRUE.equals(state.get(Provider.YOUTUBE))
        );
    }
}
