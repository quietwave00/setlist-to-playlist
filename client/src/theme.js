const withAlpha = (hex, alpha) => {
    const normalized = hex.replace('#', '');
    const value = normalized.length === 3
        ? normalized.split('').map((char) => char + char).join('')
        : normalized;

    const red = parseInt(value.slice(0, 2), 16);
    const green = parseInt(value.slice(2, 4), 16);
    const blue = parseInt(value.slice(4, 6), 16);

    return `rgba(${red}, ${green}, ${blue}, ${alpha})`;
};

const createTheme = (tokens) => ({
    ...tokens,
    primarySoft: withAlpha(tokens.primary, 0.4),
    primaryShadow: withAlpha(tokens.primary, 0.25),
    primaryHoverShadow: withAlpha(tokens.primary, 0.35),
    focusRing: withAlpha(tokens.primary, 0.14),
    focusGlow: withAlpha(tokens.primary, 0.10),
    focusBorder: withAlpha(tokens.primary, 0.38),
    activeBg: withAlpha(tokens.primary, 0.10),
    hoverBg: withAlpha(tokens.primary, 0.07),
    dropdownBorder: withAlpha(tokens.primary, 0.20),
    dropdownShadow: withAlpha(tokens.primary, 0.12),
    primaryReadable: tokens.primaryDark
});

export const themes = {
    spotify: createTheme({
        primary: '#1DB954',
        primaryHover: '#1ed760',
        primaryDark: '#169c46',

        bg: '#ffffff',
        surface: '#f5f5f5',

        text: '#2c2c2c',
        textSecondary: '#666666',

        disabledBg: '#e6e6e6',
        disabledBorder: '#d0d0d0',
        disabledText: '#9a9a9a'
    }),

    youtube: createTheme({
        primary: '#FF0000',
        primaryHover: '#ff1a1a',
        primaryDark: '#cc0000',

        bg: '#ffffff',
        surface: '#f5f5f5',

        text: '#000000',
        textSecondary: '#666666',

        disabledBg: '#e6e6e6',
        disabledBorder: '#d0d0d0',
        disabledText: '#9a9a9a'
    })
};
