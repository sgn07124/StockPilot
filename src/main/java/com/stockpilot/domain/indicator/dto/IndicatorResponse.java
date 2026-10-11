package com.stockpilot.domain.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorResponse {

    private String symbol;
    private String name;
    private String timeframe;
    private List<IndicatorItemDto> indicators;
    private IndicatorSummaryDto latestSummary;

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class IndicatorSummaryDto {
        private String latestDate;
        private BigDecimal sma5;
        private BigDecimal sma20;
        private BigDecimal sma60;
        private BigDecimal sma120;
        private BigDecimal rsi14;
        private String rsiStatus;      // 과매수, 과매도, 중립
        private String signalType;     // GOLDEN_CROSS, DEAD_CROSS, NONE
        private String signalDescription;
    }
}
