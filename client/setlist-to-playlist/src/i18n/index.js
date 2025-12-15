import { useState } from "react";

const translations = {
    ko: {
        mode: {
            spotify: 'Spotify',
            youtube: 'YouTube'
        },
        login: '로그인',
        logout: '로그아웃',
        loginFailed: '로그인에 실패했습니다.',
        inputPlaceholder: 'https://www.setlist.fm/setlist/...',
        inputDisabled: '로그인 후 이용해주세요',
        createButton: '플레이리스트 생성',
        title: 'Setlist2Playlist',
        subtitle: 'Create your playlist from any setlist.',
        createPlaylist: '플레이리스트가 생성되었습니다.'
    },
    en: {
        mode: {
            spotify: 'Spotify',
            youtube: 'YouTube'
        },
        login: 'Login',
        logout: 'Logout',
        loginFailed: 'Login Failed.',
        inputPlaceholder: 'https://www.setlist.fm/setlist/...',
        inputDisabled: 'Please Login to continue.',
        createButton: 'Create Playlist',
        title: 'Setlist2Playlist',
        subtitle: 'Create your playlist from any setlist.',
        createPlaylist: 'Your playlist is ready.'
    }
};

export const useTranslation = () => {
    const [locale] = useState('ko');
    return (key) => {
        const keys = key.split('.');
        let value = translations[locale];
        for (const k of keys) {
            value = value?.[k];
        }
        return value || key;
    };
};