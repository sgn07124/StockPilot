package com.stockpilot.common.exception;

public class InvalidCredentialsException extends BusinessException {

    public InvalidCredentialsException() {
        super(ErrorCode.AUTH_INVALID_CREDENTIALS);
    }

    public InvalidCredentialsException(String message) {
        super(ErrorCode.AUTH_INVALID_CREDENTIALS, message);
    }
}
