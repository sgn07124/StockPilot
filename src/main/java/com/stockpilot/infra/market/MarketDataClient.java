package com.stockpilot.infra.market;

import com.stockpilot.domain.stock.dto.CandleDto;
import com.stockpilot.domain.stock.dto.StockPriceResponse;
import com.stockpilot.domain.stock.entity.Timeframe;

import java.time.LocalDate;
import java.util.List;

public interface MarketDataClient {

    /**
     * 특정 종목의 현재가 시세 조회
     */
    StockPriceResponse fetchCurrentPrice(String symbol);

    /**
     * 특정 종목의 과거 일봉(Daily) 캔들 목록 조회
     */
    List<CandleDto> fetchDailyCandles(String symbol, LocalDate from, LocalDate to);

    /**
     * 특정 종목의 당일 분봉(Intraday) 캔들 목록 조회 (DB 비저장 메모리 통과용)
     */
    List<CandleDto> fetchIntradayCandles(String symbol, Timeframe timeframe);
}
