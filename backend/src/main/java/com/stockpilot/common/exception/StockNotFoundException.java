package com.stockpilot.common.exception;

public class StockNotFoundException extends BusinessException {

    public StockNotFoundException() {
        super(ErrorCode.STOCK_NOT_FOUND);
    }

    public StockNotFoundException(String symbol) {
        super(ErrorCode.STOCK_NOT_FOUND, "종목 코드를 찾을 수 없습니다: " + symbol);
    }
}
