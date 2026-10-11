package com.stockpilot.domain.indicator.service;

import com.stockpilot.domain.indicator.entity.SignalType;
import com.stockpilot.domain.indicator.entity.TechnicalIndicator;
import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.Stock;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class IndicatorCalculationService {

    private static final int RSI_PERIOD = 14;

    /**
     * 일봉 캔들 리스트(날짜 오름차순 정렬)를 기반으로 이동평균선, RSI, 시그널을 연산
     */
    public List<TechnicalIndicator> calculateIndicators(Stock stock, List<DailyCandle> sortedCandles) {
        if (sortedCandles == null || sortedCandles.isEmpty()) {
            return List.of();
        }

        List<TechnicalIndicator> results = new ArrayList<>(sortedCandles.size());
        List<Long> closePrices = sortedCandles.stream().map(DailyCandle::getClosePrice).toList();

        BigDecimal prevSma5 = null;
        BigDecimal prevSma20 = null;

        for (int i = 0; i < sortedCandles.size(); i++) {
            DailyCandle candle = sortedCandles.get(i);

            BigDecimal sma5 = calculateSma(closePrices, i, 5);
            BigDecimal sma20 = calculateSma(closePrices, i, 20);
            BigDecimal sma60 = calculateSma(closePrices, i, 60);
            BigDecimal sma120 = calculateSma(closePrices, i, 120);
            BigDecimal rsi14 = calculateRsi(closePrices, i, RSI_PERIOD);

            SignalType signalType = detectCrossover(prevSma5, prevSma20, sma5, sma20);

            TechnicalIndicator indicator = TechnicalIndicator.builder()
                    .stock(stock)
                    .indicatorDate(candle.getCandleDate())
                    .sma5(sma5)
                    .sma20(sma20)
                    .sma60(sma60)
                    .sma120(sma120)
                    .rsi14(rsi14)
                    .signalType(signalType)
                    .build();

            results.add(indicator);

            prevSma5 = sma5;
            prevSma20 = sma20;
        }

        return results;
    }

    /**
     * N일 단순이동평균 (SMA) 연산
     */
    public BigDecimal calculateSma(List<Long> closes, int currentIndex, int period) {
        if (currentIndex + 1 < period) {
            return null;
        }

        long sum = 0;
        for (int i = currentIndex - period + 1; i <= currentIndex; i++) {
            sum += closes.get(i);
        }

        return BigDecimal.valueOf(sum)
                .divide(BigDecimal.valueOf(period), 2, RoundingMode.HALF_UP);
    }

    /**
     * N일 상대강도지수 (RSI) 연산
     */
    public BigDecimal calculateRsi(List<Long> closes, int currentIndex, int period) {
        if (currentIndex < period) {
            return null; // 최소 period + 1개의 캔들 필요
        }

        double totalGain = 0.0;
        double totalLoss = 0.0;

        for (int i = currentIndex - period + 1; i <= currentIndex; i++) {
            long diff = closes.get(i) - closes.get(i - 1);
            if (diff > 0) {
                totalGain += diff;
            } else {
                totalLoss += Math.abs(diff);
            }
        }

        double avgGain = totalGain / period;
        double avgLoss = totalLoss / period;

        if (avgLoss == 0.0) {
            return BigDecimal.valueOf(100.00).setScale(2, RoundingMode.HALF_UP);
        }
        if (avgGain == 0.0) {
            return BigDecimal.valueOf(0.00).setScale(2, RoundingMode.HALF_UP);
        }

        double rs = avgGain / avgLoss;
        double rsi = 100.0 - (100.0 / (1.0 + rs));

        return BigDecimal.valueOf(rsi).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * 5일선과 20일선의 크로스오버 시그널 감지
     */
    public SignalType detectCrossover(BigDecimal prev5, BigDecimal prev20, BigDecimal cur5, BigDecimal cur20) {
        if (prev5 == null || prev20 == null || cur5 == null || cur20 == null) {
            return SignalType.NONE;
        }

        // 5일선이 20일선을 상향 돌파 (골든크로스)
        if (prev5.compareTo(prev20) <= 0 && cur5.compareTo(cur20) > 0) {
            return SignalType.GOLDEN_CROSS;
        }

        // 5일선이 20일선을 하향 돌파 (데드크로스)
        if (prev5.compareTo(prev20) >= 0 && cur5.compareTo(cur20) < 0) {
            return SignalType.DEAD_CROSS;
        }

        return SignalType.NONE;
    }
}
