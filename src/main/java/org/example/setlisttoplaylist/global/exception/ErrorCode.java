package org.example.setlisttoplaylist.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    UNKNOWN_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 오류가 발생했습니다."),
    TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "인증 토큰이 만료되었습니다."),
    SCRAPING_FAILED(HttpStatus.BAD_GATEWAY, "setlist.fm 페이지를 분석하는 중 오류가 발생했습니다."),
    AUTHENTICATE_FAILED(HttpStatus.UNAUTHORIZED, "인증에 실패하였습니다.")
    ;

    private final HttpStatus status;
    private final String message;

}
