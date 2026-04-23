import { useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import { fetchAuthUrl, logout as logoutApi } from '../utils/api.js';

const LoginButton = () => {
    const { mode, session, setSession, theme, showToast } = useAppStore();
    const t = useTranslation();
    const [isHovered, setIsHovered] = useState(false);
    const [isLoading, setIsLoading] = useState(false);

    const isLoggedInForMode =
        session.authenticated && session.activeProvider === mode;

    const handleLogin = async () => {
        if (isLoggedInForMode || isLoading) {
            return;
        }
        try {
            setIsLoading(true);
            const provider = mode === 'youtube' ? 'youtube' : 'spotify';
            const authUrl = await fetchAuthUrl(provider);
            window.location.assign(authUrl);
        } catch {
            showToast(t('loginFailed'));
        } finally {
            setIsLoading(false);
        }
    };

    const handleLogout = async () => {
        if (!isLoggedInForMode || isLoading) return;
        try {
            setIsLoading(true);
            await logoutApi();
            setSession({
                authenticated: false,
                activeProvider: null
            });
        } catch {
            showToast(t('loginFailed'));
        } finally {
            setIsLoading(false);
        }
    };

    const styles = {
        button: {
            padding: '10px 24px',
            border: 'none',
            borderRadius: '10px',
            fontSize: '14px',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.2s ease',
            backgroundColor: theme.primary,
            color: '#fff',
            boxShadow: isHovered ? '0 4px 16px rgba(0, 0, 0, 0.3)' : '0 2px 8px rgba(0, 0, 0, 0.2)',
            transform: isHovered ? 'translateY(-2px)' : 'translateY(0)'
        }
    };

    return (
        <button
            style={styles.button}
            onClick={isLoggedInForMode ? handleLogout : handleLogin}
            onMouseEnter={() => setIsHovered(true)}
            onMouseLeave={() => setIsHovered(false)}
            disabled={isLoading}
        >
            {isLoggedInForMode ? t('logout') : t('login')}
        </button>
    );
};

export default LoginButton;
