package com.stockpilot.domain.indicator;

import com.stockpilot.domain.indicator.entity.SignalType;
import com.stockpilot.domain.indicator.service.IndicatorCalculationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class IndicatorCalculationTest {

    private final IndicatorCalculationService service = new IndicatorCalculationService();

    @Test
    @DisplayName("5일 단순이동평균(SMA5)이 정확하게 계산된다")
    void calculateSma5() {
        List<Long> closes = List.of(100L, 200L, 300L, 400L, 500L); // sum=1500, avg=300

        BigDecimal sma5 = service.calculateSma(closes, 4, 5);

        assertThat(sma5).isEqualByComparingTo(new BigDecimal("300.00"));
    }

    @Test
    @DisplayName("데이터 개수가 기간보다 적으면 SMA는 null을 반환한다")
    void calculateSmaInsufficientData() {
        List<Long> closes = List.of(100L, 200L, 300L);

        BigDecimal sma5 = service.calculateSma(closes, 2, 5);

        assertThat(sma5).isNull();
    }

    @Test
    @DisplayName("14일 연속 상승 시 RSI는 100을 반환한다")
    void calculateRsiAllGains() {
        // 15개 데이터 (14번 상승)
        List<Long> closes = List.of(
                10L, 20L, 30L, 40L, 50L, 60L, 70L, 80L, 90L, 100L, 110L, 120L, 130L, 140L, 150L
        );

        BigDecimal rsi = service.calculateRsi(closes, 14, 14);

        assertThat(rsi).isEqualByComparingTo(new BigDecimal("100.00"));
    }

    @Test
    @DisplayName("14일 연속 하락 시 RSI는 0을 반환한다")
    void calculateRsiAllLosses() {
        List<Long> closes = List.of(
                150L, 140L, 130L, 120L, 110L, 100L, 90L, 80L, 70L, 60L, 50L, 40L, 30L, 20L, 10L
        );

        BigDecimal rsi = service.calculateRsi(closes, 14, 14);

        assertThat(rsi).isEqualByComparingTo(new BigDecimal("0.00"));
    }

    @Test
    @DisplayName("5일선이 20일선을 상향 돌파하면 골든크로스를 감지한다")
    void detectGoldenCross() {
        BigDecimal prev5 = new BigDecimal("49000");
        BigDecimal prev20 = new BigDecimal("50000"); // 5일선이 20일선 아래

        BigDecimal cur5 = new BigDecimal("51000");
        BigDecimal cur20 = new BigDecimal("50500"); // 5일선이 20일선 위로 돌파

        SignalType signal = service.detectCrossover(prev5, prev20, cur5, cur20);

        assertThat(signal).isEqualTo(SignalType.GOLDEN_CROSS);
    }

    @Test
    @DisplayName("5일선이 20일선을 하향 돌파하면 데드크로스를 감지한다")
    void detectDeadCross() {
        BigDecimal prev5 = new BigDecimal("51000");
        BigDecimal prev20 = new BigDecimal("50000");

        BigDecimal cur5 = new BigDecimal("49500");
        BigDecimal cur20 = new BigDecimal("50200");

        SignalType signal = service.detectCrossover(prev5, prev20, cur5, cur20);

        assertThat(signal).isEqualTo(SignalType.DEAD_CROSS);
    }
}
