package com.stockpilot.infra.market;

import com.stockpilot.domain.stock.dto.CandleDto;
import com.stockpilot.domain.stock.dto.StockPriceResponse;
import com.stockpilot.domain.stock.entity.Timeframe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Slf4j
@Component("mockMarketDataClient")
public class MockMarketDataClient implements MarketDataClient {

    private static final Map<String, StockMeta> KNOWN_STOCKS = Map.of(
            "005930", new StockMeta("삼성전자", "KOSPI", 72000L),
            "000660", new StockMeta("SK하이닉스", "KOSPI", 185000L),
            "035420", new StockMeta("NAVER", "KOSPI", 178000L),
            "035720", new StockMeta("카카오", "KOSPI", 39500L),
            "005380", new StockMeta("현대차", "KOSPI", 245000L)
    );

    @Override
    public StockPriceResponse fetchCurrentPrice(String symbol) {
        StockMeta meta = getMeta(symbol);
        Random random = new Random(symbol.hashCode() ^ LocalDate.now().toEpochDay());

        long currentPrice = meta.basePrice() + (random.nextInt(21) - 10) * (meta.basePrice() / 100);
        long openPrice = meta.basePrice() + (random.nextInt(11) - 5) * (meta.basePrice() / 100);
        long highPrice = Math.max(currentPrice, openPrice) + random.nextInt(1000);
        long lowPrice = Math.min(currentPrice, openPrice) - random.nextInt(1000);
        long changePrice = currentPrice - meta.basePrice();
        BigDecimal changeRate = BigDecimal.valueOf((double) changePrice / meta.basePrice() * 100)
                .setScale(2, RoundingMode.HALF_UP);
        long volume = 5_000_000L + random.nextInt(10_000_000);

        return StockPriceResponse.builder()
                .symbol(symbol)
                .name(meta.name())
                .market(meta.market())
                .currentPrice(currentPrice)
                .changePrice(changePrice)
                .changeRate(changeRate)
                .openPrice(openPrice)
                .highPrice(highPrice)
                .lowPrice(lowPrice)
                .volume(volume)
                .build();
    }

    @Override
    public List<CandleDto> fetchDailyCandles(String symbol, LocalDate from, LocalDate to) {
        log.info("[MockMarketData] Fetching daily candles for {} from {} to {}", symbol, from, to);
        StockMeta meta = getMeta(symbol);
        List<CandleDto> candles = new ArrayList<>();

        long price = meta.basePrice();
        LocalDate cur = from;

        while (!cur.isAfter(to)) {
            // 주말 제외
            if (cur.getDayOfWeek() != DayOfWeek.SATURDAY && cur.getDayOfWeek() != DayOfWeek.SUNDAY) {
                // 일자별 결정론적 시드
                Random dayRandom = new Random(symbol.hashCode() ^ cur.toEpochDay());
                double changeRatio = (dayRandom.nextDouble() * 0.06) - 0.028; // -2.8% ~ +3.2%
                long close = Math.round(price * (1.0 + changeRatio));
                long open = Math.round(price * (1.0 + (dayRandom.nextDouble() * 0.02 - 0.01)));
                long high = Math.max(open, close) + Math.round(price * (dayRandom.nextDouble() * 0.015));
                long low = Math.min(open, close) - Math.round(price * (dayRandom.nextDouble() * 0.015));
                long volume = 1_000_000L + Math.round(dayRandom.nextDouble() * 15_000_000);
                long tradingValue = close * volume;

                candles.add(CandleDto.builder()
                        .time(cur.toString())
                        .open(open)
                        .high(high)
                        .low(low)
                        .close(close)
                        .volume(volume)
                        .tradingValue(tradingValue)
                        .build());

                price = close;
            }
            cur = cur.plusDays(1);
        }

        return candles;
    }

    @Override
    public List<CandleDto> fetchIntradayCandles(String symbol, Timeframe timeframe) {
        log.info("[MockMarketData] Fetching intraday candles for {} (timeframe: {})", symbol, timeframe.getCode());
        StockMeta meta = getMeta(symbol);
        List<CandleDto> candles = new ArrayList<>();

        int stepMinutes = switch (timeframe) {
            case M1 -> 1;
            case M5 -> 5;
            case M15 -> 15;
            case H1 -> 60;
            default -> 5;
        };

        LocalDate today = LocalDate.now();
        LocalTime marketOpen = LocalTime.of(9, 0);
        LocalTime marketClose = LocalTime.of(15, 30);

        LocalTime curTime = marketOpen;
        long price = meta.basePrice();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        while (!curTime.isAfter(marketClose)) {
            Random tickRandom = new Random(symbol.hashCode() ^ curTime.toNanoOfDay());
            double changeRatio = (tickRandom.nextDouble() * 0.01) - 0.005;
            long close = Math.round(price * (1.0 + changeRatio));
            long open = price;
            long high = Math.max(open, close) + Math.round(price * (tickRandom.nextDouble() * 0.003));
            long low = Math.min(open, close) - Math.round(price * (tickRandom.nextDouble() * 0.003));
            long volume = 10_000L + Math.round(tickRandom.nextDouble() * 100_000);

            LocalDateTime dateTime = LocalDateTime.of(today, curTime);
            candles.add(CandleDto.builder()
                    .time(dateTime.format(formatter))
                    .open(open)
                    .high(high)
                    .low(low)
                    .close(close)
                    .volume(volume)
                    .tradingValue(close * volume)
                    .build());

            price = close;
            curTime = curTime.plusMinutes(stepMinutes);
        }

        return candles;
    }

    private StockMeta getMeta(String symbol) {
        return KNOWN_STOCKS.getOrDefault(symbol,
                new StockMeta("종목-" + symbol, "KOSPI", 50000L));
    }

    private record StockMeta(String name, String market, Long basePrice) {}
}
