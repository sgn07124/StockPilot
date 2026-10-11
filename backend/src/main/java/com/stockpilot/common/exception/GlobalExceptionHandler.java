package com.stockpilot.common.exception;

import com.stockpilot.common.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 1. 비즈니스 커스텀 예외 처리
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        ErrorCode errorCode = e.getErrorCode();
        log.warn("[BusinessException] Code: {}, Message: {}", errorCode.getCode(), e.getMessage());

        return ResponseEntity
                .status(errorCode.getHttpStatus())
                .body(ApiResponse.error(errorCode, e.getMessage(), null));
    }

    /**
     * 2. @Valid 입력값 검증 실패 처리
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.warn("[Validation Exception] Total errors: {}", e.getBindingResult().getErrorCount());

        List<FieldErrorDetail> fieldErrors = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> new FieldErrorDetail(
                        fieldError.getField(),
                        fieldError.getRejectedValue() != null ? fieldError.getRejectedValue().toString() : null,
                        fieldError.getDefaultMessage()))
                .collect(Collectors.toList());

        return ResponseEntity
                .status(ErrorCode.COMMON_INVALID_INPUT.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.COMMON_INVALID_INPUT, fieldErrors));
    }

    /**
     * 3. 요청 파라미터 타입 불일치 (예: Long 자리에 문자열 전달)
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleTypeMismatchException(MethodArgumentTypeMismatchException e) {
        log.warn("[TypeMismatch] Name: {}, RequiredType: {}", e.getName(), e.getRequiredType());
        return ResponseEntity
                .status(ErrorCode.COMMON_TYPE_MISMATCH.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.COMMON_TYPE_MISMATCH, null));
    }

    /**
     * 4. 필수 쿼리 파라미터 누락
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Void>> handleMissingParamException(MissingServletRequestParameterException e) {
        log.warn("[MissingParam] ParamName: {}", e.getParameterName());
        return ResponseEntity
                .status(ErrorCode.COMMON_MISSING_PARAMETER.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.COMMON_MISSING_PARAMETER, e.getParameterName() + " 파라미터가 누락되었습니다.", null));
    }

    /**
     * 5. 지원하지 않는 HTTP 메서드
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("[MethodNotSupported] Method: {}", e.getMethod());
        return ResponseEntity
                .status(ErrorCode.COMMON_METHOD_NOT_ALLOWED.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.COMMON_METHOD_NOT_ALLOWED, null));
    }

    /**
     * 6. 존재하지 않는 엔드포인트/정적 리소스 (404)
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNoResourceFoundException(NoResourceFoundException e) {
        log.warn("[NoResourceFound] Path: {}", e.getResourcePath());
        return ResponseEntity
                .status(ErrorCode.STOCK_NOT_FOUND.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.STOCK_NOT_FOUND, "요청한 리소스를 찾을 수 없습니다: " + e.getResourcePath(), null));
    }

    /**
     * 7. 최상위 미처리 서버 예외 (500)
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception e) {
        log.error("[Unhandled Exception] Unexpected server error occurred", e);
        return ResponseEntity
                .status(ErrorCode.COMMON_INTERNAL_SERVER_ERROR.getHttpStatus())
                .body(ApiResponse.error(ErrorCode.COMMON_INTERNAL_SERVER_ERROR, null));
    }

    public record FieldErrorDetail(String field, String rejectedValue, String reason) {}
}
