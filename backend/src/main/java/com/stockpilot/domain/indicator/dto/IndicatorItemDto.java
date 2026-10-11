package com.stockpilot.domain.indicator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IndicatorItemDto {

    private String time;
    private BigDecimal sma5;
    private BigDecimal sma20;
    private BigDecimal sma60;
    private BigDecimal sma120;
    private BigDecimal rsi14;
    private String signalType;
}
