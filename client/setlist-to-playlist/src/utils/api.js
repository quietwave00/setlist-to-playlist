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

/**
 * Playlist
 */
export function createPlaylistFromSetlist({ url, provider }) {
    const params = new URLSearchParams({
        url,
        provider
    });
    return request(`/music?${params.toString()}`, {
        method: 'GET'
    });
}
