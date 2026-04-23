import React, { useState, createContext, useContext, useEffect, useCallback } from 'react';
import { themes } from "../theme.js";
import { fetchSessionStatus } from "../utils/api.js";

const AppContext = createContext();

export const AppProvider = ({ children }) => {
    const [mode, setMode] = useState('spotify');
    const [session, setSession] = useState({
        authenticated: false,
        activeProvider: null
    });
    const [user, setUser] = useState(null);
    const [playlistResult, setPlaylistResult] = useState(null);
    const [selectedSetlist, setSelectedSetlist] = useState(null);
    const [toast, setToast] = useState(null);

    const showToast = useCallback((message, type = 'error') => {
        setToast({
            id: Date.now(),
            message,
            type
        });
    }, []);

    useEffect(() => {
        const applySession = (status) => {
            setSession({
                authenticated: Boolean(status.authenticated),
                activeProvider: status.activeProvider ?? null
            });
            if (status.activeProvider) {
                setMode(status.activeProvider);
            }
        };

        const loadSession = async ({ retry = false } = {}) => {
            const maxAttempts = retry ? 4 : 1;

            for (let attempt = 1; attempt <= maxAttempts; attempt += 1) {
                try {
                    const status = await fetchSessionStatus();
                    applySession(status);
                    if (!retry || status.authenticated) {
                        return;
                    }
                } catch {
                    if (attempt === maxAttempts) {
                        setSession({
                            authenticated: false,
                            activeProvider: null
                        });
                        return;
                    }
                }

                await new Promise((resolve) => {
                    window.setTimeout(resolve, 250);
                });
            }
        };

        const url = new URL(window.location.href);
        const hasAuthCallbackParams = ['authProvider', 'code', 'state']
            .some(key => url.searchParams.get(key));

        loadSession({ retry: hasAuthCallbackParams }).finally(() => {
            // After an auth callback, clean up the URL to avoid repeated refreshes.
            if (hasAuthCallbackParams) {
                window.history.replaceState({}, document.title, window.location.pathname);
            }
        });
    }, []);

    const isActiveProviderAuthenticated =
        session.authenticated && session.activeProvider === mode;
    const canUseCurrentMode =
        mode === 'spotify' || isActiveProviderAuthenticated;

    return (
        <AppContext.Provider value={{
            mode,
            setMode,
            session,
            setSession,
            isActiveProviderAuthenticated,
            canUseCurrentMode,
            user,
            setUser,
            playlistResult,
            setPlaylistResult,
            selectedSetlist,
            setSelectedSetlist,
            toast,
            setToast,
            showToast,
            theme: themes[mode]
        }}>
            {children}
        </AppContext.Provider>
    );
};

export const useAppStore = () => useContext(AppContext);
