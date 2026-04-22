package org.example.setlisttoplaylist.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Unknown server error."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Authentication token has expired."),
    SCRAPING_FAILED(HttpStatus.BAD_GATEWAY, "Failed to parse setlist.fm page."),
    AUTHENTICATE_FAILED(HttpStatus.UNAUTHORIZED, "Authentication failed."),
    INVALID_PROVIDER(HttpStatus.BAD_REQUEST, "Invalid provider."),

    SETLIST_FM_INVALID_URL(HttpStatus.BAD_REQUEST, "Invalid setlist.fm URL."),
    SETLIST_FM_INVALID_SEARCH_QUERY(HttpStatus.BAD_REQUEST, "Enter at least one setlist search condition."),
    SETLIST_FM_FORBIDDEN(HttpStatus.BAD_GATEWAY, "setlist.fm API returned 403 (forbidden)."),
    SETLIST_FM_NOT_FOUND(HttpStatus.NOT_FOUND, "setlist.fm API returned 404 (not found)."),
    SETLIST_FM_EMPTY_SONG_LIST(HttpStatus.BAD_GATEWAY, "setlist.fm returned an empty song list."),
    SETLIST_FM_UPSTREAM_ERROR(HttpStatus.BAD_GATEWAY, "setlist.fm API request failed.");

    private final HttpStatus status;
    private final String message;
}
