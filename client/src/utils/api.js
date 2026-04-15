const API_BASE = '/api';

/**
 * 공통 fetch 래퍼
 */
async function request(path, options = {}) {
    const res = await fetch(`${API_BASE}${path}`, {
        credentials: 'include',
        headers: {
            'Content-Type': 'application/json',
            ...(options.headers || {})
        },
        ...options
    });

    let json;
    try {
        json = await res.json();
    } catch {
        throw new Error('Invalid JSON response');
    }

    if (!res.ok || json.success === false) {
        const message =
            json?.message ||
            `API Error (${res.status})`;

        const error = new Error(message);
        error.status = res.status;
        error.errorCode = json?.errorCode;
        throw error;
    }
    return json.data;
}

/**
 * Auth
 */
export function fetchAuthUrl(provider) {
    return request(`/auth/${provider}`, {
        method: 'GET'
    });
}
export function fetchSessionStatus() {
    return request('/auth/session', {
        method: 'GET'
    });
}

export function logout() {
    return request('/auth/logout', {
        method: 'POST'
    });
}

export function refreshSpotifyAccessToken() {
    return request('/spotify/token/refresh', {
        method: 'POST'
    });
}

/**
 * Playlist
 */
export function createPlaylistFromSetlist({ url, provider }) {
    const params = new URLSearchParams({
        url,
        provider
    });
    return request(`/music/create?${params.toString()}`, {
        method: 'GET'
    });
}

export function isAuthError(error) {
    const status = error?.status;
    const code = error?.errorCode;
    if (status === 401) return true;
    if (typeof code === 'object' && code?.status === 'UNAUTHORIZED') return true;

    const message = (error?.message || '').toLowerCase();
    return message.includes('auth');
}
