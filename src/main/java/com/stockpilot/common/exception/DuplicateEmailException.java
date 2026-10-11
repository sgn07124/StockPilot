package com.stockpilot.common.exception;

public class DuplicateEmailException extends BusinessException {

    public DuplicateEmailException() {
        super(ErrorCode.AUTH_DUPLICATE_EMAIL);
    }

    public DuplicateEmailException(String email) {
        super(ErrorCode.AUTH_DUPLICATE_EMAIL, "이미 존재하는 이메일입니다: " + email);
    }
}
