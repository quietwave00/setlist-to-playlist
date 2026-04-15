import React, { useState, createContext, useContext, useEffect } from 'react';
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

    useEffect(() => {
        const loadSession = () => fetchSessionStatus()
            .then(status => {
                setSession({
                    authenticated: Boolean(status.authenticated),
                    activeProvider: status.activeProvider ?? null
                });
                if (status.activeProvider) {
                    setMode(status.activeProvider);
                }
            })
            .catch(() => {
                setSession({
                    authenticated: false,
                    activeProvider: null
                });
            });

        const url = new URL(window.location.href);
        const hasAuthCallbackParams = ['authProvider', 'code', 'state']
            .some(key => url.searchParams.get(key));

        loadSession().finally(() => {
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
            theme: themes[mode]
        }}>
            {children}
        </AppContext.Provider>
    );
};

export const useAppStore = () => useContext(AppContext);
