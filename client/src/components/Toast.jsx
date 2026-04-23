import { useEffect } from 'react';
import { useAppStore } from '../store/useAppStore.jsx';

const Toast = () => {
    const { toast, setToast, theme } = useAppStore();

    useEffect(() => {
        if (!toast) {
            return undefined;
        }

        const timer = window.setTimeout(() => {
            setToast(null);
        }, 1500);

        return () => window.clearTimeout(timer);
    }, [setToast, toast]);

    if (!toast) {
        return null;
    }

    return (
        <div
            role="alert"
            aria-live="assertive"
            style={{
                position: 'fixed',
                top: '50%',
                left: '50%',
                transform: 'translate(-50%, -50%) scale(1)',
                zIndex: 1000,
                maxWidth: 'min(420px, calc(100vw - 40px))',
                padding: '14px 18px',
                borderRadius: '14px',
                border: `1px solid ${theme.dropdownBorder}`,
                background: 'rgba(255,255,255,0.96)',
                color: theme.primaryReadable,
                boxShadow: `0 14px 40px ${theme.dropdownShadow}, 0 8px 24px rgba(0,0,0,0.12)`,
                backdropFilter: 'blur(12px)',
                fontSize: '14px',
                fontWeight: 400,
                textAlign: 'center',
                lineHeight: 1.4,
                pointerEvents: 'none',
                animation: 'toastFade 1.5s ease forwards'
            }}
        >
            <style>
                {`
                    @keyframes toastFade {
                        0% {
                            opacity: 0;
                            transform: translate(-50%, -50%) scale(0.98);
                        }
                        12% {
                            opacity: 1;
                            transform: translate(-50%, -50%) scale(1);
                        }
                        80% {
                            opacity: 1;
                            transform: translate(-50%, -50%) scale(1);
                        }
                        100% {
                            opacity: 0;
                            transform: translate(-50%, -50%) scale(0.98);
                        }
                    }
                `}
            </style>
            {toast.message}
        </div>
    );
};

export default Toast;
