package org.example.setlisttoplaylist.global.exception;

import lombok.extern.slf4j.Slf4j;
import org.example.setlisttoplaylist.global.response.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ApiResponse<?>> handleApiException(ApiException e) {
        e.printStackTrace();
        log.warn("API exception occurred. errorCode={}, message={}",
                e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.fail(e.getErrorCode()));
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ApiResponse<?>> handleCustomException(CustomException e) {
        e.printStackTrace();
        log.warn("Custom exception occurred. errorCode={}, message={}",
                e.getErrorCode(), e.getErrorCode().getMessage());
        return ResponseEntity
                .status(e.getErrorCode().getStatus())
                .body(ApiResponse.fail(e.getErrorCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<?>> handleException(Exception e) {
        log.error("Unhandled exception occurred.", e);
        return ResponseEntity
                .internalServerError()
                .body(ApiResponse.fail(e));
    }
}
