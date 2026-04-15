package org.example.setlisttoplaylist.auth.domain;

public enum Provider {
    SPOTIFY("spotify", "ROLE_SPOTIFY"),
    YOUTUBE("youtube", "ROLE_YOUTUBE");

    private final String key;
    private final String role;

    Provider(String key, String role) {
        this.key = key;
        this.role = role;
    }

    public String key() {
        return key;
    }

    public String role() {
        return role;
    }

    public static Provider fromKey(String key) {
        for (Provider provider : values()) {
            if (provider.key.equalsIgnoreCase(key)) {
                return provider;
            }
        }
        throw new IllegalArgumentException("Unknown provider: " + key);
    }
}
