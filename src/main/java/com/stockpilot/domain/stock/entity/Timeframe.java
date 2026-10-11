package com.stockpilot.domain.stock.entity;

import com.stockpilot.common.exception.InvalidTimeframeException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Timeframe {
    M1("1m", false, "1분봉"),
    M5("5m", false, "5분봉"),
    M15("15m", false, "15분봉"),
    H1("1h", false, "1시간봉"),
    D1("1D", true, "일봉"),
    W1("1W", true, "주봉"),
    MN1("1M", true, "월봉");

    private final String code;
    private final boolean persistent;
    private final String description;

    public static Timeframe from(String code) {
        if (code == null || code.isBlank()) {
            return D1;
        }
        return Arrays.stream(values())
                .filter(t -> t.code.equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new InvalidTimeframeException(code));
    }
}
