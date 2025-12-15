package org.example.setlisttoplaylist.global.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.example.setlisttoplaylist.global.exception.ErrorCode;

@Getter
@RequiredArgsConstructor
public class ApiResponse<T> {

    private final boolean success;
    private final String message;
    private final T data;
    private final ErrorCode errorCode;

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(true, "ok", data, null);
    }

    public static ApiResponse<Void> success() {
        return new ApiResponse<>(true, "ok", null, null);
    }

    public static ApiResponse<?> fail(ErrorCode errorCode) {
        return new ApiResponse<>(false, errorCode.getMessage(), null, errorCode);
    }

    public static ApiResponse<?> fail(Exception e) {
        return new ApiResponse<>(false, e.getMessage(), null, ErrorCode.UNKNOWN_ERROR);
    }
}