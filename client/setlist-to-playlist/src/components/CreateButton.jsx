import { useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import { createPlaylistFromSetlist } from '../utils/api.js';

const CreateButton = () => {
    const { isActiveProviderAuthenticated, mode, theme, setPlaylistResult } = useAppStore();
    const t = useTranslation();
    const [isHovered, setIsHovered] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleCreate = async () => {
        if (!isActiveProviderAuthenticated || isLoading) return;
        const input = document.getElementById('setlist-url-input');
        const url = input?.value?.trim();

        if (!url) {
            setError(t('inputDisabled'));
            return;
        }

        try {
            setIsLoading(true);
            setError(null);
            setPlaylistResult(null);
            const result = await createPlaylistFromSetlist({ url, provider: mode });
            setPlaylistResult(result);
        } catch (err) {
            setError(err.message || 'Request failed');
        } finally {
            setIsLoading(false);
        }
    };

    const styles = {
        button: {
            minWidth: '160px',
            padding: '16px 24px',
            borderRadius: '12px',
            fontSize: '16px',
            fontWeight: '600',
            whiteSpace: 'nowrap',

            border: `2px solid ${
                isActiveProviderAuthenticated
                    ? theme.primary
                    : theme.disabledBorder
            }`,

            backgroundColor: isActiveProviderAuthenticated
                ? theme.primary
                : theme.disabledBg,

            color: isActiveProviderAuthenticated
                ? '#ffffff'
                : theme.disabledText,

            cursor: isActiveProviderAuthenticated && !isLoading ? 'pointer' : 'not-allowed',
            opacity: 1,

            transition: 'all 0.2s ease',

            boxShadow: isHovered && isActiveProviderAuthenticated
                ? '0 6px 18px rgba(0, 0, 0, 0.18)'
                : '0 2px 8px rgba(0, 0, 0, 0.12)',

            transform: isHovered && isActiveProviderAuthenticated
                ? 'translateY(-1px)'
                : 'translateY(0)'
        }
    };

    return (
        <>
            <button
                style={styles.button}
                onClick={handleCreate}
                disabled={!isActiveProviderAuthenticated || isLoading}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
            >
                {isLoading ? 'Loading...' : t('createButton')}
            </button>

            {error && (
                <div style={{ marginTop: 8, color: 'red', fontSize: 12 }}>
                    {error}
                </div>
            )}
        </>
    );
};

export default CreateButton;
