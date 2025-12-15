import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from "../i18n/index.js";

const PlaylistResult = () => {
    const { playlistResult, theme } = useAppStore();
    const t = useTranslation();

    if (!playlistResult) return null;

    const styles = {
        card: {
            marginTop: '16px',
            padding: '16px',
            borderRadius: '12px',
            border: `1px solid ${theme.primary}`,
            backgroundColor: theme.surface || '#fff',
            boxShadow: '0 6px 18px rgba(0,0,0,0.08)'
        },
        title: {
            margin: 0,
            marginBottom: '8px',
            fontSize: '16px',
            fontWeight: '700',
            color: theme.text || '#111'
        },
        link: {
            color: theme.primary,
            fontWeight: '600',
            textDecoration: 'none',
            wordBreak: 'break-all'
        },
        provider: {
            fontSize: '12px',
            color: theme.textSecondary || '#666',
            marginBottom: '6px'
        }
    };

    return (
        <div style={styles.card}>
            <p style={styles.title}>{t('createPlaylist')}</p>
            <div style={styles.provider}>
                Provider: {playlistResult.provider}
            </div>
            <a
                href={playlistResult.playlistUrl}
                target="_blank"
                rel="noreferrer"
                style={styles.link}
            >
                {playlistResult.playlistUrl}
            </a>
        </div>
    );
};

export default PlaylistResult;
