package com.stockpilot.common.exception;

public class InvalidTimeframeException extends BusinessException {

    public InvalidTimeframeException() {
        super(ErrorCode.STOCK_INVALID_TIMEFRAME);
    }

    public InvalidTimeframeException(String timeframe) {
        super(ErrorCode.STOCK_INVALID_TIMEFRAME, "지원하지 않는 차트 주기(Timeframe)입니다: " + timeframe);
    }
}
