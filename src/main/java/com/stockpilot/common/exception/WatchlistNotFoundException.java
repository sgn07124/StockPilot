package com.stockpilot.common.exception;

public class WatchlistNotFoundException extends BusinessException {

    public WatchlistNotFoundException() {
        super(ErrorCode.WATCHLIST_NOT_FOUND);
    }

    public WatchlistNotFoundException(String symbol) {
        super(ErrorCode.WATCHLIST_NOT_FOUND, "등록되지 않은 관심 종목입니다: " + symbol);
    }
}
