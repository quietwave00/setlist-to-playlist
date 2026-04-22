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
        inputPlaceholder: '아티스트 이름으로 검색',
        inputDisabled: '로그인 후 이용해주세요',
        searchButton: '검색',
        searchEmpty: '검색 결과가 없습니다.',
        searchFailed: '셋리스트 검색에 실패했습니다.',
        selectedSetlist: '선택됨',
        noSetlistSelected: '셋리스트를 선택해주세요.',
        prevPage: '<',
        nextPage: '>',
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
        inputPlaceholder: 'Search setlists by artist',
        inputDisabled: 'Please Login to continue.',
        searchButton: 'Search',
        searchEmpty: 'No setlists found.',
        searchFailed: 'Failed to search setlists.',
        selectedSetlist: 'Selected',
        noSetlistSelected: 'Please select a setlist.',
        prevPage: '<',
        nextPage: '>',
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
