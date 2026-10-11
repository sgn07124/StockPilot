package com.stockpilot.domain.stock.controller;

import com.stockpilot.common.response.ApiResponse;
import com.stockpilot.domain.stock.dto.StockCandleResponse;
import com.stockpilot.domain.stock.dto.StockPriceResponse;
import com.stockpilot.domain.stock.dto.StockSummaryResponse;
import com.stockpilot.domain.stock.service.StockService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class StockApiController {

    private final StockService stockService;

    /**
     * 지원 주식 종목 목록 조회
     */
    @GetMapping
    public ApiResponse<List<StockSummaryResponse>> getSupportedStocks() {
        return ApiResponse.success(stockService.getSupportedStocks());
    }

    /**
     * 특정 종목 기본 정보 및 현재가 시세 조회
     */
    @GetMapping("/{symbol}")
    public ApiResponse<StockPriceResponse> getStockPrice(@PathVariable("symbol") String symbol) {
        return ApiResponse.success(stockService.getStockPrice(symbol));
    }

    /**
     * 차트 캔들스틱 데이터 조회 (Timeframe별 온디맨드 백필 및 분봉 실시간 반환)
     *
     * @param symbol    종목코드 (예: 005930)
     * @param timeframe 1m, 5m, 15m, 1h, 1D, 1W, 1M (기본값: 1D)
     * @param from      시작일자 (YYYY-MM-DD)
     * @param to        종료일자 (YYYY-MM-DD)
     */
    @GetMapping("/{symbol}/candles")
    public ApiResponse<StockCandleResponse> getCandles(
            @PathVariable("symbol") String symbol,
            @RequestParam(name = "timeframe", defaultValue = "1D") String timeframe,
            @RequestParam(name = "from", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(name = "to", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ApiResponse.success(stockService.getCandles(symbol, timeframe, from, to));
    }
}
