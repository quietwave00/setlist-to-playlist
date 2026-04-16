import { useState } from 'react';
import { Music, Video } from 'lucide-react';
import { useAppStore } from "../../store/useAppStore.jsx";
import { useTranslation } from "../../i18n/index.js";

const themes = {
    spotify: { primary: '#38c56a' },
    youtube: { primary: '#ff4242' }
};

const ToggleMode = () => {
    const { mode, setMode, theme, setPlaylistResult } = useAppStore();
    const t = useTranslation();
    const [hoveredMode, setHoveredMode] = useState(null);

    const styles = {
        container: {
            display: 'flex',
            gap: '2px',
            padding: '3px',
            background: 'linear-gradient(145deg, #f8f9fa, #e9ecef)',
            borderRadius: '16px',
            boxShadow: 'inset 0 1px 3px rgba(0, 0, 0, 0.08)',
            position: 'relative'
        },
        option: (currentMode) => ({
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            gap: '10px',
            padding: '12px 28px',
            border: 'none',
            borderRadius: '13px',
            fontSize: '14px',
            fontWeight: '600',
            cursor: 'pointer',
            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
            backgroundColor: mode === currentMode
                ? themes[currentMode].primary
                : hoveredMode === currentMode
                    ? 'rgba(255, 255, 255, 0.6)'
                    : 'transparent',
            color: mode === currentMode ? '#fff' : theme.textSecondary,
            boxShadow: mode === currentMode
                ? `0 4px 16px ${themes[currentMode].primary}40, 0 2px 8px rgba(0, 0, 0, 0.1)`
                : hoveredMode === currentMode
                    ? '0 2px 8px rgba(0, 0, 0, 0.05)'
                    : 'none',
            transform: mode === currentMode
                ? 'translateY(-1px) scale(1.02)'
                : hoveredMode === currentMode
                    ? 'translateY(-0.5px)'
                    : 'translateY(0)',
            position: 'relative',
            overflow: 'visible',
            backdropFilter: mode !== currentMode && hoveredMode === currentMode ? 'blur(10px)' : 'none',
            filter: mode === currentMode
                ? `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='dust'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='1.8' numOctaves='6' seed='5'/%3E%3CfeDisplacementMap in='SourceGraphic' scale='3' xChannelSelector='R' yChannelSelector='G'/%3E%3CfeGaussianBlur stdDeviation='0.3'/%3E%3C/filter%3E%3C/svg%3E#dust")`
                : 'none',
            WebkitFilter: mode === currentMode
                ? `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='dust'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='1.8' numOctaves='6' seed='5'/%3E%3CfeDisplacementMap in='SourceGraphic' scale='3' xChannelSelector='R' yChannelSelector='G'/%3E%3CfeGaussianBlur stdDeviation='0.3'/%3E%3C/filter%3E%3C/svg%3E#dust")`
                : 'none'
        }),
        iconWrapper: {
            display: 'flex',
            alignItems: 'center',
            transition: 'transform 0.3s ease',
            position: 'relative',
            zIndex: 2
        },
        dustParticle: (currentMode, index) => {
            const isActive = mode === currentMode;
            const color = themes[currentMode].primary;
            const positions = [
                { top: '-8px', left: '10%', size: '6px', opacity: 0.3 },
                { top: '-5px', right: '15%', size: '4px', opacity: 0.4 },
                { bottom: '-6px', left: '20%', size: '5px', opacity: 0.25 },
                { bottom: '-4px', right: '25%', size: '3px', opacity: 0.35 },
                { top: '50%', left: '-8px', size: '4px', opacity: 0.3 },
                { top: '30%', right: '-6px', size: '5px', opacity: 0.28 }
            ];
            const pos = positions[index];

            return {
                position: 'absolute',
                width: pos.size,
                height: pos.size,
                borderRadius: '50%',
                backgroundColor: color,
                opacity: isActive ? pos.opacity : 0,
                transition: 'all 0.4s ease',
                ...pos,
                transform: isActive ? 'scale(1)' : 'scale(0.5)',
                filter: 'blur(0.5px)',
                pointerEvents: 'none'
            };
        },
        paintSplatter: (currentMode, index) => {
            const isActive = mode === currentMode || hoveredMode === currentMode;
            const color = themes[currentMode].primary;
            const splatters = [
                { top: '-10px', left: '30%', width: '12px', height: '8px', rotate: '25deg', opacity: 0.15 },
                { bottom: '-8px', right: '35%', width: '10px', height: '10px', rotate: '-15deg', opacity: 0.12 },
                { top: '20%', left: '-12px', width: '8px', height: '12px', rotate: '45deg', opacity: 0.18 }
            ];
            const splat = splatters[index];

            return {
                position: 'absolute',
                width: splat.width,
                height: splat.height,
                background: `radial-gradient(ellipse at center, ${color} 0%, transparent 70%)`,
                opacity: isActive ? splat.opacity : 0,
                transition: 'all 0.5s cubic-bezier(0.4, 0, 0.2, 1)',
                ...splat,
                transform: `rotate(${splat.rotate}) scale(${isActive ? 1 : 0.3})`,
                filter: 'blur(1px)',
                pointerEvents: 'none',
                borderRadius: '40% 60% 50% 50%'
            };
        },
        buttonWrapper: {
            position: 'relative',
            display: 'flex'
        }
    };

    const handleModeChange = (nextMode) => {
        if (nextMode === mode) return;
        setMode(nextMode);
        setPlaylistResult(null); // reset playlist result when switching providers
    };

    return (
        <div style={styles.container}>
            <div style={styles.buttonWrapper}>
                <button
                    style={styles.option('spotify')}
                    onClick={() => handleModeChange('spotify')}
                    onMouseEnter={() => setHoveredMode('spotify')}
                    onMouseLeave={() => setHoveredMode(null)}
                >
                    <div style={{
                        ...styles.iconWrapper,
                        transform: hoveredMode === 'spotify' ? 'rotate(5deg) scale(1.1)' : 'rotate(0deg) scale(1)'
                    }}>
                        <Music size={18} strokeWidth={2.5} />
                    </div>
                    <span style={{ letterSpacing: '0.3px', position: 'relative', zIndex: 2 }}>{t('mode.spotify')}</span>

                    {/* Dust particles */}
                    {[0, 1, 2, 3, 4, 5].map(i => (
                        <div key={`dust-${i}`} style={styles.dustParticle('spotify', i)} />
                    ))}

                    {/* Paint splatters */}
                    {[0, 1, 2].map(i => (
                        <div key={`splat-${i}`} style={styles.paintSplatter('spotify', i)} />
                    ))}
                </button>
            </div>

            <div style={styles.buttonWrapper}>
                <button
                    style={styles.option('youtube')}
                    onClick={() => handleModeChange('youtube')}
                    onMouseEnter={() => setHoveredMode('youtube')}
                    onMouseLeave={() => setHoveredMode(null)}
                >
                    <div style={{
                        ...styles.iconWrapper,
                        transform: hoveredMode === 'youtube' ? 'rotate(-5deg) scale(1.1)' : 'rotate(0deg) scale(1)'
                    }}>
                        <Video size={18} strokeWidth={2.5} />
                    </div>
                    <span style={{ letterSpacing: '0.3px', position: 'relative', zIndex: 2 }}>{t('mode.youtube')}</span>

                    {/* Dust particles */}
                    {[0, 1, 2, 3, 4, 5].map(i => (
                        <div key={`dust-${i}`} style={styles.dustParticle('youtube', i)} />
                    ))}

                    {/* Paint splatters */}
                    {[0, 1, 2].map(i => (
                        <div key={`splat-${i}`} style={styles.paintSplatter('youtube', i)} />
                    ))}
                </button>
            </div>
        </div>
    );
};

export default ToggleMode;
