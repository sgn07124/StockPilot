package com.stockpilot.common.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.stockpilot.common.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {

    private final boolean success;
    private final T data;
    private final String message;
    private final ErrorDetail error;
    private final LocalDateTime timestamp;

    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message("SUCCESS")
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .success(true)
                .data(data)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> ok() {
        return success(null);
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, Object details) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .error(new ErrorDetail(errorCode.getCode(), errorCode.getMessage(), details))
                .timestamp(LocalDateTime.now())
                .build();
    }

    public static <T> ApiResponse<T> error(ErrorCode errorCode, String customMessage, Object details) {
        return ApiResponse.<T>builder()
                .success(false)
                .data(null)
                .error(new ErrorDetail(errorCode.getCode(), customMessage, details))
                .timestamp(LocalDateTime.now())
                .build();
    }

    @Getter
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class ErrorDetail {
        private final String code;
        private final String message;
        private final Object details;

        public ErrorDetail(String code, String message, Object details) {
            this.code = code;
            this.message = message;
            this.details = details;
        }
    }
}
