package com.stockpilot.domain.stock.dto;

import com.stockpilot.domain.stock.entity.Stock;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StockSummaryResponse {

    private String symbol;
    private String name;
    private String market;

    public static StockSummaryResponse from(Stock stock) {
        return StockSummaryResponse.builder()
                .symbol(stock.getSymbol())
                .name(stock.getName())
                .market(stock.getMarket().name())
                .build();
    }
}
