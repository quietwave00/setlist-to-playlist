import { useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import { createPlaylistFromSetlist } from '../utils/api.js';

const CreateButton = () => {
    const { canUseCurrentMode, mode, theme, selectedSetlist, setPlaylistResult, showToast } = useAppStore();
    const t = useTranslation();
    const [isHovered, setIsHovered] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const canCreate = canUseCurrentMode && !isLoading;

    const handleCreate = async () => {
        if (!canUseCurrentMode || isLoading) return;
        const setlistId = selectedSetlist?.id;

        if (!setlistId) {
            showToast(t('noSetlistSelected'));
            return;
        }

        try {
            setIsLoading(true);
            setPlaylistResult(null);
            const result = await createPlaylistFromSetlist({ url: setlistId, provider: mode });
            setPlaylistResult(result);
        } catch (err) {
            showToast(err.message || 'Request failed');
        } finally {
            setIsLoading(false);
        }
    };

    const loadingDots = (
        <span
            aria-label="Loading"
            style={{
                display: 'inline-flex',
                alignItems: 'center',
                gap: '3px',
                height: '1em',
            }}
        >
            <style>
                {`
                    @keyframes createButtonDotBounce {
                        0%, 80%, 100% {
                            transform: translateY(0);
                            opacity: 0.55;
                        }
                        40% {
                            transform: translateY(-4px);
                            opacity: 1;
                        }
                    }
                `}
            </style>
            {[0, 1, 2].map((index) => (
                <span
                    key={index}
                    style={{
                        display: 'inline-block',
                        animation: 'createButtonDotBounce 0.9s ease-in-out infinite',
                        animationDelay: `${index * 0.14}s`,
                    }}
                >
                    .
                </span>
            ))}
        </span>
    );

    return (
        <>
            <button
                onClick={handleCreate}
                disabled={!canCreate}
                onMouseEnter={() => setIsHovered(true)}
                onMouseLeave={() => setIsHovered(false)}
                style={{
                    display: 'flex',
                    alignItems: 'center',
                    justifyContent: 'center',
                    gap: '10px',
                    height: '46px',
                    boxSizing: 'border-box',
                    padding: '12px 28px',
                    border: 'none',
                    borderRadius: '13px',
                    fontSize: '14px',
                    fontWeight: '600',
                    cursor: !canCreate ? 'not-allowed' : 'pointer',
                    transition: '0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                    backgroundColor: !canCreate ? theme.primarySoft : theme.primary,
                    color: 'rgb(255, 255, 255)',
                    boxShadow: !canCreate
                        ? 'none'
                        : isHovered
                            ? `${theme.primaryHoverShadow} 0px 6px 24px, rgba(0, 0, 0, 0.12) 0px 4px 12px`
                            : `${theme.primaryShadow} 0px 4px 16px, rgba(0, 0, 0, 0.1) 0px 2px 8px`,
                    transform: !canCreate
                        ? 'none'
                        : isHovered
                            ? 'translateY(-1px) scale(1.02)'
                            : 'translateY(0px) scale(1)',
                    position: 'relative',
                    overflow: 'visible',
                    backdropFilter: 'none',
                    opacity: !canCreate ? 0.6 : 1,
                    filter: `url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='dust'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='1.8' numOctaves='6' seed='5'/%3E%3CfeDisplacementMap in='SourceGraphic' scale='3' xChannelSelector='R' yChannelSelector='G'/%3E%3CfeGaussianBlur stdDeviation='0.3'/%3E%3C/filter%3E%3C/svg%3E#dust")`,
                    alignSelf: 'flex-start',
                }}
            >
                {isLoading ? (
                    <span
                        style={{
                            display: 'inline-flex',
                            alignItems: 'center',
                            gap: '4px',
                        }}
                    >
                        Loading{loadingDots}
                    </span>
                ) : t('createButton')}
            </button>
        </>
    );
};

export default CreateButton;
