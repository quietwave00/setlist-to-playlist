const SPOTIFY_REFRESH_BUFFER_MS = 60_000;

export function computeExpiresAt(expiresInSeconds) {
    const seconds = Number.isFinite(expiresInSeconds) ? expiresInSeconds : 0;
    return Date.now() + seconds * 1000;
}

export function isSpotifyTokenStale(expiresAt) {
    if (!expiresAt) return true;
    return (expiresAt - SPOTIFY_REFRESH_BUFFER_MS) <= Date.now();
}

export function shouldRunSpotifySilentAuth(mode, status) {
    if (mode !== 'spotify') return false;
    return status === 'idle' || status === 'failed';
}

export function readCachedSpotifyAuth() {
    try {
        const raw = window.localStorage.getItem('spotifyAuth');
        if (!raw) return null;
        const parsed = JSON.parse(raw);
        if (!parsed?.accessToken || !parsed?.expiresAt) {
            return null;
        }
        return parsed;
    } catch {
        return null;
    }
}

export function writeCachedSpotifyAuth(accessToken, expiresAt) {
    try {
        window.localStorage.setItem('spotifyAuth', JSON.stringify({ accessToken, expiresAt }));
    } catch {
        // Swallow storage errors – token caching is a best-effort optimization.
    }
}

export function clearCachedSpotifyAuth() {
    try {
        window.localStorage.removeItem('spotifyAuth');
    } catch {
        // ignore
    }
}

export function isSpotifyAuthUsable(status) {
    return status !== 'failed';
}

export { SPOTIFY_REFRESH_BUFFER_MS };
