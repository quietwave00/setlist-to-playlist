import React, {useState, createContext, useContext, useEffect} from 'react';
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
        fetchSessionStatus()
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
    }, []);

    const isActiveProviderAuthenticated =
        session.authenticated && session.activeProvider === mode;

    return (
        <AppContext.Provider value={{
            mode,
            setMode,
            session,
            setSession,
            isActiveProviderAuthenticated,
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
