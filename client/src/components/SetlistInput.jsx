import { useEffect, useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import { searchSetlists } from '../utils/api.js';

const formatSetlistLabel = (setlist) => {
    return [
        setlist.artistName,
        setlist.eventDate,
        setlist.venueName
    ].filter(Boolean).join(' · ');
};

const SetlistInput = () => {
    const {
        canUseCurrentMode,
        mode,
        selectedSetlist,
        setSelectedSetlist,
        setPlaylistResult
    } = useAppStore();
    const t = useTranslation();
    const [query, setQuery] = useState('');
    const [results, setResults] = useState([]);
    const [isSearching, setIsSearching] = useState(false);
    const [error, setError] = useState(null);
    const [hasSearched, setHasSearched] = useState(false);
    const [isDropdownOpen, setIsDropdownOpen] = useState(false);
    const [page, setPage] = useState(1);
    const [pagination, setPagination] = useState({
        total: 0,
        page: 1,
        itemsPerPage: 20
    });

    const normalizedQuery = query.trim();
    const canSearch = canUseCurrentMode && normalizedQuery.length >= 2;
    const totalPages = Math.max(1, Math.ceil(pagination.total / Math.max(1, pagination.itemsPerPage)));
    const canGoPrevious = !isSearching && pagination.page > 1;
    const canGoNext = !isSearching && pagination.page < totalPages;
    const firstItem = pagination.total === 0
        ? 0
        : (pagination.page - 1) * pagination.itemsPerPage + 1;
    const lastItem = Math.min(pagination.total, pagination.page * pagination.itemsPerPage);
    const shouldShowPagination = !error && !isSearching && pagination.total > pagination.itemsPerPage;
    const shouldShowDropdown =
        isDropdownOpen &&
        canUseCurrentMode &&
        (isSearching || error || results.length > 0 || (hasSearched && normalizedQuery.length >= 2));
    const placeholder =
        mode === 'youtube' && !canUseCurrentMode
            ? t('inputDisabled')
            : t('inputPlaceholder');

    useEffect(() => {
        setQuery('');
        setResults([]);
        setError(null);
        setHasSearched(false);
        setIsDropdownOpen(false);
        setPage(1);
        setPagination({
            total: 0,
            page: 1,
            itemsPerPage: 20
        });
        setSelectedSetlist(null);
    }, [mode, setSelectedSetlist]);

    useEffect(() => {
        if (selectedSetlist && query === formatSetlistLabel(selectedSetlist)) {
            setResults([]);
            setError(null);
            setHasSearched(false);
            setIsSearching(false);
            setIsDropdownOpen(false);
            setPage(1);
            setPagination({
                total: 0,
                page: 1,
                itemsPerPage: 20
            });
            return;
        }

        if (!canSearch) {
            setResults([]);
            setError(null);
            setHasSearched(false);
            setIsSearching(false);
            setPage(1);
            setPagination({
                total: 0,
                page: 1,
                itemsPerPage: 20
            });
            return;
        }

        const controller = new AbortController();
        const timer = window.setTimeout(async () => {
            try {
                setIsSearching(true);
                setError(null);
                setHasSearched(true);
                setResults([]);
                const data = await searchSetlists({
                    artistName: normalizedQuery,
                    page,
                    signal: controller.signal
                });
                if (!controller.signal.aborted) {
                    setResults(data.setlists || []);
                    setPagination({
                        total: data.total || 0,
                        page: data.page || page,
                        itemsPerPage: data.itemsPerPage || 20
                    });
                    setIsDropdownOpen(true);
                }
            } catch (err) {
                if (err.name === 'AbortError') {
                    return;
                }

                if (!controller.signal.aborted) {
                    const isEmptySearchResult =
                        err.status === 404 ||
                        String(err.message || '').includes('setlist.fm API returned 404');

                    setResults([]);
                    setPagination({
                        total: 0,
                        page,
                        itemsPerPage: 20
                    });
                    setError(isEmptySearchResult ? null : err.message || 'Failed to search setlists.');
                    setIsDropdownOpen(true);
                }
            } finally {
                if (!controller.signal.aborted) {
                    setIsSearching(false);
                }
            }
        }, 350);

        return () => {
            controller.abort();
            window.clearTimeout(timer);
        };
    }, [canSearch, normalizedQuery, page, query, selectedSetlist]);

    const handleSelect = (setlist) => {
        setSelectedSetlist(setlist);
        setPlaylistResult(null);
        setQuery(formatSetlistLabel(setlist));
        setIsDropdownOpen(false);
    };

    return (
        <div style={{
            position: 'relative',
            width: '100%',
            flex: 1,
            minWidth: 0,
        }}>
            <input
                type="search"
                id="setlist-search-input"
                placeholder={placeholder}
                disabled={!canUseCurrentMode}
                value={query}
                onChange={(e) => {
                    setQuery(e.target.value);
                    setPage(1);
                    setSelectedSetlist(null);
                    setIsDropdownOpen(true);
                }}
                onFocus={(e) => {
                    if (canUseCurrentMode) {
                        e.target.style.boxShadow = '0 0 0 3px rgba(56, 197, 106, 0.25), 0 4px 16px rgba(56, 197, 106, 0.15)';
                        e.target.style.borderColor = 'rgba(56, 197, 106, 0.6)';
                        e.target.style.background = 'rgba(255,255,255,0.98)';
                        setIsDropdownOpen(true);
                    }
                }}
                onBlur={(e) => {
                    e.target.style.boxShadow = '0 2px 8px rgba(0,0,0,0.06)';
                    e.target.style.borderColor = 'rgba(0,0,0,0.1)';
                    e.target.style.background = 'rgba(255,255,255,0.92)';
                    window.setTimeout(() => setIsDropdownOpen(false), 120);
                }}
                style={{
                    width: '100%',
                    height: '46px',
                    padding: '12px 18px',
                    fontSize: '14px',
                    fontWeight: '500',
                    border: '1.5px solid rgba(0,0,0,0.1)',
                    borderRadius: '13px',
                    outline: 'none',
                    background: 'rgba(255,255,255,0.92)',
                    color: '#1a1a1a',
                    boxShadow: '0 2px 8px rgba(0,0,0,0.06)',
                    transition: 'box-shadow 0.25s cubic-bezier(0.4,0,0.2,1), border-color 0.25s cubic-bezier(0.4,0,0.2,1), background 0.25s',
                    boxSizing: 'border-box',
                    opacity: !canUseCurrentMode ? 0.45 : 1,
                    cursor: !canUseCurrentMode ? 'not-allowed' : 'text',
                }}
            />

            {shouldShowDropdown && (
                <div style={{
                    position: 'absolute',
                    top: 'calc(100% + 8px)',
                    left: 0,
                    right: 0,
                    background: 'rgba(255,255,255,0.97)',
                    border: '1.5px solid rgba(56, 197, 106, 0.2)',
                    borderRadius: '13px',
                    boxShadow: '0 8px 32px rgba(56,197,106,0.12), 0 4px 16px rgba(0,0,0,0.08)',
                    zIndex: 100,
                    overflow: 'hidden',
                    backdropFilter: 'blur(12px)'
                }}>
                    {isSearching && (
                        <div style={{
                            padding: '14px 18px',
                            fontSize: '13px',
                            fontWeight: '500',
                            letterSpacing: '0.01em',
                        }}>Searching...</div>
                    )}
                    {error && (
                        <div style={{
                            padding: '14px 18px',
                            fontSize: '13px',
                            color: '#e05252',
                            fontWeight: '500',
                        }}>{error}</div>
                    )}
                    {!error && hasSearched && !isSearching && results.length === 0 && (
                        <div style={{
                            padding: '14px 18px',
                            fontSize: '13px',
                            color: 'rgba(0,0,0,0.4)',
                            fontWeight: '500',
                        }}>{t('searchEmpty')}</div>
                    )}
                    {results.length > 0 && (
                        <div style={{
                            maxHeight: '260px',
                            overflowY: 'auto',
                        }}>
                            {results.map((setlist) => {
                                const isSelected = selectedSetlist?.id === setlist.id;
                                const location = [
                                    setlist.venueName,
                                    setlist.cityName,
                                    setlist.countryName
                                ].filter(Boolean).join(', ');
                                const details = [
                                    setlist.eventDate,
                                    location,
                                    setlist.tourName
                                ].filter(Boolean).join(' · ');

                                return (
                                    <button
                                        key={setlist.id}
                                        type="button"
                                        onMouseDown={(e) => e.preventDefault()}
                                        onClick={() => handleSelect(setlist)}
                                        onMouseEnter={(e) => {
                                            if (!isSelected) {
                                                e.currentTarget.style.background = 'rgba(56,197,106,0.07)';
                                            }
                                        }}
                                        onMouseLeave={(e) => {
                                            if (!isSelected) {
                                                e.currentTarget.style.background = 'transparent';
                                            }
                                        }}
                                        style={{
                                            display: 'flex',
                                            flexDirection: 'column',
                                            gap: '3px',
                                            width: '100%',
                                            padding: '11px 18px',
                                            border: 'none',
                                            borderTop: '1px solid rgba(0,0,0,0.05)',
                                            background: isSelected ? 'rgba(56,197,106,0.1)' : 'transparent',
                                            cursor: 'pointer',
                                            textAlign: 'left',
                                            transition: 'background 0.15s ease',
                                            borderLeft: isSelected ? '3px solid rgb(56,197,106)' : '3px solid transparent',
                                        }}
                                    >
                                        <span style={{
                                            fontSize: '14px',
                                            fontWeight: '600',
                                            color: isSelected ? 'rgb(56,197,106)' : '#1a1a1a',
                                            lineHeight: 1.3,
                                        }}>
                                            {setlist.artistName || 'Unknown artist'}
                                        </span>
                                        <span style={{
                                            fontSize: '12px',
                                            fontWeight: '400',
                                            color: 'rgba(0,0,0,0.45)',
                                            lineHeight: 1.4,
                                        }}>
                                            {details || setlist.url}
                                        </span>
                                    </button>
                                );
                            })}
                        </div>
                    )}
                    {shouldShowPagination && (
                        <div
                            onMouseDown={(e) => e.preventDefault()}
                            style={{
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'space-between',
                                gap: '10px',
                                padding: '9px 12px',
                                borderTop: '1px solid rgba(0,0,0,0.06)',
                                background: 'rgba(255,255,255,0.88)',
                            }}
                        >
                            <button
                                type="button"
                                disabled={!canGoPrevious}
                                onClick={() => setPage((current) => Math.max(1, current - 1))}
                                style={{
                                    padding: '7px 11px',
                                    border: 'none',
                                    background: 'transparent',
                                    fontSize: '12px',
                                    fontWeight: '1000',
                                    cursor: canGoPrevious ? 'pointer' : 'not-allowed',
                                }}
                            >
                                {t('prevPage')}
                            </button>
                            <div style={{
                                flex: 1,
                                textAlign: 'center',
                                fontSize: '12px',
                                fontWeight: '500',
                                color: 'rgba(0,0,0,0.46)',
                                whiteSpace: 'nowrap',
                            }}>
                                {firstItem}-{lastItem} / {pagination.total}
                            </div>
                            <button
                                type="button"
                                disabled={!canGoNext}
                                onClick={() => setPage((current) => Math.min(totalPages, current + 1))}
                                style={{
                                    padding: '7px 11px',
                                    border: 'none',
                                    background: 'transparent',
                                    fontSize: '12px',
                                    fontWeight: '1000',
                                    cursor: canGoNext ? 'pointer' : 'not-allowed',
                                }}
                            >
                                {t('nextPage')}
                            </button>
                        </div>
                    )}
                </div>
            )}
        </div>
    );
};

export default SetlistInput;
