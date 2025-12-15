// src/pages/Home.jsx
import { useAppStore } from '../store/useAppStore.jsx';
import { useTranslation } from '../i18n/index.js';
import ToggleMode from '../components/ToggleMode/ToggleMode.jsx';
import LoginButton from '../components/LoginButton.jsx';
import SetlistInput from '../components/SetlistInput.jsx';
import CreateButton from '../components/CreateButton.jsx';
import PlaylistResult from '../components/PlaylistResult.jsx';

const Home = () => {
    const { theme } = useAppStore();
    const t = useTranslation();

    const styles = {
        homePage: {
            minHeight: '100vh',
            width: '100%',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            padding: '20px',
            color: theme.text,
            transition: 'background-color 0.3s ease'
        },
        container: {
            maxWidth: '800px',
            width: '100%'
        },
        header: {
            textAlign: 'center',
            marginBottom: '48px'
        },
        title: {
            fontSize: '48px',
            fontWeight: '800',
            marginBottom: '8px',
            letterSpacing: '-0.02em',
            position: 'relative',
            color: theme.text,
            filter: `
                url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='dust'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='2.5' numOctaves='8' seed='2'/%3E%3CfeDisplacementMap in='SourceGraphic' scale='8' xChannelSelector='R' yChannelSelector='G'/%3E%3CfeGaussianBlur stdDeviation='0.5'/%3E%3CfeMorphology operator='erode' radius='0.5'/%3E%3C/filter%3E%3C/svg%3E#dust")
                contrast(1.4)
                brightness(0.95)
                `,
            WebkitFilter: `
                url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Cfilter id='dust'%3E%3CfeTurbulence type='fractalNoise' baseFrequency='2.5' numOctaves='8' seed='2'/%3E%3CfeDisplacementMap in='SourceGraphic' scale='8' xChannelSelector='R' yChannelSelector='G'/%3E%3CfeGaussianBlur stdDeviation='0.5'/%3E%3CfeMorphology operator='erode' radius='0.5'/%3E%3C/filter%3E%3C/svg%3E#dust")
                contrast(1.4)
                brightness(0.95)
                `
        },
        subtitle: {
            fontSize: '16px',
            fontWeight: '400',
            color: theme.textSecondary
        },
        content: {
            display: 'flex',
            flexDirection: 'column',
            gap: '24px'
        },
        controlsRow: {
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            gap: '16px'
        },
        inputRow: {
            display: 'flex',
            gap: '12px',
            alignItems: 'stretch'
        }
    };

    return (
        <div style={styles.homePage}>
            <div style={styles.container}>
                <header style={styles.header}>
                    <h1 style={styles.title}>
                        <span style={styles.title}>{t('title')}</span>
                    </h1>
                    <p style={styles.subtitle}>{t('subtitle')}</p>
                </header>

                <div style={styles.content}>
                    <div style={styles.controlsRow}>
                        <ToggleMode />
                        <LoginButton />
                    </div>

                    <div style={styles.inputRow}>
                        <SetlistInput />
                        <CreateButton />
                    </div>
                    <PlaylistResult />
                </div>
            </div>
        </div>
    );
};

export default Home;
