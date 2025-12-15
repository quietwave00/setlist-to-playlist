// src/components/SetlistInput.jsx
import { useState } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';

const SetlistInput = () => {
    const { isActiveProviderAuthenticated, theme } = useAppStore();
    const t = useTranslation();
    const [value, setValue] = useState('');

    const styles = {
        container: {
            flex: 1,
            display: 'flex'
        },
        input: {
            width: '100%',
            padding: '16px 20px',
            border: `2px solid ${isActiveProviderAuthenticated ? theme.primary : theme.textSecondary}`,
            borderRadius: '12px',
            fontSize: '16px',
            transition: 'all 0.3s ease',
            outline: 'none',
            backgroundColor: theme.surface,
            color: theme.text,
            opacity: isActiveProviderAuthenticated ? 1 : 0.5,
            cursor: isActiveProviderAuthenticated ? 'text' : 'not-allowed'
        }
    };

    return (
        <div style={styles.container}>
            <input
                type="text"
                style={styles.input}
                id="setlist-url-input"
                placeholder={isActiveProviderAuthenticated ? t('inputPlaceholder') : t('inputDisabled')}
                disabled={!isActiveProviderAuthenticated}
                value={value}
                onChange={(e) => setValue(e.target.value)}
                onFocus={(e) => {
                    if (isActiveProviderAuthenticated) {
                        e.target.style.boxShadow = '0 0 0 4px rgba(0, 0, 0, 0.05)';
                    }
                }}
                onBlur={(e) => {
                    e.target.style.boxShadow = 'none';
                }}
            />
        </div>
    );
};

export default SetlistInput;
