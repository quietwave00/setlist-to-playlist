import { useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import { createPlaylistFromSetlist } from '../utils/api.js';

const CreateButton = () => {
    const { canUseCurrentMode, mode, selectedSetlist, setPlaylistResult } = useAppStore();
    const t = useTranslation();
    const [isHovered, setIsHovered] = useState(false);
    const [isLoading, setIsLoading] = useState(false);
    const [error, setError] = useState(null);
    const canCreate = canUseCurrentMode && Boolean(selectedSetlist) && !isLoading;

    const handleCreate = async () => {
        if (!canUseCurrentMode || isLoading) return;
        const setlistId = selectedSetlist?.id;

        if (!setlistId) {
            setError(t('noSetlistSelected'));
            return;
        }

        try {
            setIsLoading(true);
            setError(null);
            setPlaylistResult(null);
            const result = await createPlaylistFromSetlist({ url: setlistId, provider: mode });
            setPlaylistResult(result);
        } catch (err) {
            setError(err.message || 'Request failed');
        } finally {
            setIsLoading(false);
        }
    };

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
                    backgroundColor: !canCreate ? 'rgba(56,197,106,0.4)' : 'rgb(56, 197, 106)',
                    color: 'rgb(255, 255, 255)',
                    boxShadow: !canCreate
                        ? 'none'
                        : isHovered
                            ? 'rgba(56, 197, 106, 0.35) 0px 6px 24px, rgba(0, 0, 0, 0.12) 0px 4px 12px'
                            : 'rgba(56, 197, 106, 0.25) 0px 4px 16px, rgba(0, 0, 0, 0.1) 0px 2px 8px',
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
                {isLoading ? 'Loading...' : t('createButton')}
            </button>

            {error && (
                <div style={{
                    marginTop: 8,
                    fontSize: 12,
                    fontWeight: '500',
                    color: '#e05252',
                    padding: '6px 10px',
                    background: 'rgba(224,82,82,0.07)',
                    borderRadius: '8px',
                    border: '1px solid rgba(224,82,82,0.15)',
                }}>
                    {error}
                </div>
            )}
        </>
    );
};

export default CreateButton;
