package com.stockpilot.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockPriceResponse {

    private String symbol;
    private String name;
    private String market;
    private Long currentPrice;
    private Long changePrice;
    private BigDecimal changeRate;
    private Long volume;
    private Long highPrice;
    private Long lowPrice;
    private Long openPrice;
}
