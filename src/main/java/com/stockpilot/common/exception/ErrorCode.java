package com.stockpilot.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // Common
    COMMON_INVALID_INPUT(HttpStatus.BAD_REQUEST, "C001", "입력 파라미터 유효성 검증에 실패했습니다."),
    COMMON_MISSING_PARAMETER(HttpStatus.BAD_REQUEST, "C002", "필수 파라미터가 누락되었습니다."),
    COMMON_TYPE_MISMATCH(HttpStatus.BAD_REQUEST, "C003", "파라미터 타입이 올바르지 않습니다."),
    COMMON_METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "C004", "지원하지 않는 HTTP 메서드입니다."),
    COMMON_INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "C005", "서버 내부 오류가 발생했습니다."),

    // Auth
    AUTH_UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "A001", "로그인이 필요한 요청입니다."),
    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "A002", "이메일 또는 비밀번호가 일치하지 않습니다."),
    AUTH_INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "A003", "유효하지 않거나 변조된 토큰입니다."),
    AUTH_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "A004", "만료된 인증 토큰입니다."),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "A005", "접근 권한이 없습니다."),
    AUTH_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "A006", "이미 사용 중인 이메일 주소입니다."),

    // Stock & Candle
    STOCK_NOT_FOUND(HttpStatus.NOT_FOUND, "S001", "요청하신 종목을 찾을 수 없습니다."),
    STOCK_INVALID_TIMEFRAME(HttpStatus.BAD_REQUEST, "S002", "지원하지 않는 차트 주기(Timeframe)입니다."),
    STOCK_EXTERNAL_API_ERROR(HttpStatus.BAD_GATEWAY, "S003", "외부 시세 API 연동에 실패했습니다."),
    STOCK_EXTERNAL_API_TIMEOUT(HttpStatus.GATEWAY_TIMEOUT, "S004", "외부 시세 API 응답 시간이 초과되었습니다."),
    STOCK_SYNC_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "S005", "시세 동기화 처리에 실패했습니다."),

    // Technical Indicator
    INDICATOR_INSUFFICIENT_DATA(HttpStatus.BAD_REQUEST, "I001", "지표 연산에 필요한 데이터가 부족합니다."),
    INDICATOR_CALCULATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "I002", "기술적 지표 연산 중 오류가 발생했습니다."),

    // Spring AI
    AI_SERVICE_UNAVAILABLE(HttpStatus.SERVICE_UNAVAILABLE, "L001", "AI 진단 서비스가 일시적으로 원활하지 않습니다."),
    AI_RESPONSE_PARSE_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "L002", "AI 분석 결과 구조화 파싱에 실패했습니다."),

    // Watchlist
    WATCHLIST_ALREADY_EXISTS(HttpStatus.CONFLICT, "W001", "이미 관심 종목에 등록되어 있습니다."),
    WATCHLIST_NOT_FOUND(HttpStatus.NOT_FOUND, "W002", "등록되지 않은 관심 종목입니다.");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;
}
