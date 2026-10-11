package com.stockpilot.common.exception;

public class DuplicateWatchlistException extends BusinessException {

    public DuplicateWatchlistException() {
        super(ErrorCode.WATCHLIST_ALREADY_EXISTS);
    }

    public DuplicateWatchlistException(String symbol) {
        super(ErrorCode.WATCHLIST_ALREADY_EXISTS, "이미 관심 종목에 등록되어 있습니다: " + symbol);
    }
}
