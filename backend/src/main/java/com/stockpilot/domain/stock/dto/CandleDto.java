package com.stockpilot.domain.stock.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CandleDto {

    private String time;
    private Long open;
    private Long high;
    private Long low;
    private Long close;
    private Long volume;
    private Long tradingValue;
}
