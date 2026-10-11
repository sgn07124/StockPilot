package com.stockpilot.domain.indicator.controller;

import com.stockpilot.common.response.ApiResponse;
import com.stockpilot.domain.indicator.dto.IndicatorResponse;
import com.stockpilot.domain.indicator.service.IndicatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/stocks")
@RequiredArgsConstructor
public class IndicatorApiController {

    private final IndicatorService indicatorService;

    /**
     * 기술적 보조지표(이동평균선, RSI, 크로스오버 시그널) 조회
     *
     * @param symbol    종목코드 (예: 005930)
     * @param timeframe 차트 주기 (기본값: 1D)
     */
    @GetMapping("/{symbol}/indicators")
    public ApiResponse<IndicatorResponse> getIndicators(
            @PathVariable("symbol") String symbol,
            @RequestParam(name = "timeframe", defaultValue = "1D") String timeframe) {

        return ApiResponse.success(indicatorService.getIndicators(symbol, timeframe));
    }
}
