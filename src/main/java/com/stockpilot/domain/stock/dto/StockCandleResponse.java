package com.stockpilot.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockCandleResponse {

    private String symbol;
    private String name;
    private String timeframe;
    private List<CandleDto> candles;
}
