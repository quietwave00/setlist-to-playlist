import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from "../i18n/index.js";

const PlaylistResult = () => {
    const { playlistResult, theme } = useAppStore();
    const t = useTranslation();

    if (!playlistResult) return null;

    const styles = {
        card: {
            padding: '24px',
            borderRadius: '16px',
            background: `linear-gradient(135deg, rgba(255, 255, 255, 0.7) 0%, rgba(248, 249, 250, 0.7) 100%)`,
            boxShadow: '0 8px 32px rgba(0,0,0,0.08)',
            position: 'relative',
            overflow: 'hidden',
            transition: 'transform 0.2s ease, box-shadow 0.2s ease'
        },
        titleSection: {
            flex: 1
        },
        title: {
            margin: 0,
            fontSize: '18px',
            fontWeight: '700',
            color: theme.text,
            letterSpacing: '-0.3px'
        },
        linkContainer: {
            padding: '16px',
            borderRadius: '12px',
            position: 'relative',
            zIndex: 1
        },
        link: {
            color: theme.primary,
            fontWeight: '600',
            textDecoration: 'none',
            textAlign: 'center',
            wordBreak: 'break-all',
            fontSize: '16px',
            display: 'block',
            transition: 'opacity 0.2s ease',
            position: 'relative',
            filter: 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cfilter id=\'dust\'%3E%3CfeTurbulence type=\'fractalNoise\' baseFrequency=\'1.8\' numOctaves=\'6\' seed=\'5\'/%3E%3CfeDisplacementMap in=\'SourceGraphic\' scale=\'2.0\' xChannelSelector=\'R\' yChannelSelector=\'G\'/%3E%3CfeGaussianBlur stdDeviation=\'0.3\'/%3E%3C/filter%3E%3C/svg%3E#dust")',
            '-webkit-filter': 'url("data:image/svg+xml,%3Csvg xmlns=\'http://www.w3.org/2000/svg\'%3E%3Cfilter id=\'dust\'%3E%3CfeTurbulence type=\'fractalNoise\' baseFrequency=\'1.8\' numOctaves=\'6\' seed=\'5\'/%3E%3CfeDisplacementMap in=\'SourceGraphic\' scale=\'2.0\' xChannelSelector=\'R\' yChannelSelector=\'G\'/%3E%3CfeGaussianBlur stdDeviation=\'0.3\'/%3E%3C/filter%3E%3C/svg%3E#dust")'
        }
    };

    const handleMouseEnter = (e) => {
        e.currentTarget.style.transform = 'translateY(-2px)';
        e.currentTarget.style.boxShadow = '0 12px 40px rgba(0,0,0,0.12)';
    };

    const handleMouseLeave = (e) => {
        e.currentTarget.style.transform = 'translateY(0)';
        e.currentTarget.style.boxShadow = '0 8px 32px rgba(0,0,0,0.08)';
    };

    const handleLinkHover = (e) => {
        e.currentTarget.style.opacity = '0.7';
    };

    const handleLinkLeave = (e) => {
        e.currentTarget.style.opacity = '1';
    };

    return (
        <div
            style={styles.card}
            onMouseEnter={handleMouseEnter}
            onMouseLeave={handleMouseLeave}
        >
            <div style={styles.linkContainer}>
                <a
                    href={playlistResult.playlistUrl}
                    target="_blank"
                    rel="noreferrer"
                    style={styles.link}
                    onMouseEnter={handleLinkHover}
                    onMouseLeave={handleLinkLeave}
                >
                    {playlistResult.playlistUrl}
                </a>
            </div>
        </div>
    );
};

export default PlaylistResult;