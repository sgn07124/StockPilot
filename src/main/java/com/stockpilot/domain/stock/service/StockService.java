package com.stockpilot.domain.stock.service;

import com.stockpilot.common.exception.StockNotFoundException;
import com.stockpilot.domain.stock.dto.CandleDto;
import com.stockpilot.domain.stock.dto.StockCandleResponse;
import com.stockpilot.domain.stock.dto.StockPriceResponse;
import com.stockpilot.domain.stock.dto.StockSummaryResponse;
import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.MarketType;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.entity.Timeframe;
import com.stockpilot.domain.stock.repository.DailyCandleRepository;
import com.stockpilot.domain.stock.repository.StockRepository;
import com.stockpilot.infra.market.MarketDataClient;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockService {

    private final StockRepository stockRepository;
    private final DailyCandleRepository dailyCandleRepository;
    private final StockSyncService stockSyncService;
    private final MarketDataClient marketDataClient;

    @PostConstruct
    @Transactional
    public void initDefaultStocks() {
        if (stockRepository.count() == 0) {
            log.info("[Stock] Initializing default stock master data");
            List<Stock> defaultStocks = List.of(
                    Stock.builder().symbol("005930").name("삼성전자").market(MarketType.KOSPI).build(),
                    Stock.builder().symbol("000660").name("SK하이닉스").market(MarketType.KOSPI).build(),
                    Stock.builder().symbol("035420").name("NAVER").market(MarketType.KOSPI).build(),
                    Stock.builder().symbol("035720").name("카카오").market(MarketType.KOSPI).build(),
                    Stock.builder().symbol("005380").name("현대차").market(MarketType.KOSPI).build()
            );
            stockRepository.saveAll(defaultStocks);
        }
    }

    @Transactional(readOnly = true)
    public List<StockSummaryResponse> getSupportedStocks() {
        return stockRepository.findAll().stream()
                .map(StockSummaryResponse::from)
                .toList();
    }

    @Transactional
    public Stock getOrCreateStock(String symbol) {
        return stockRepository.findBySymbol(symbol)
                .orElseGet(() -> {
                    StockPriceResponse price = marketDataClient.fetchCurrentPrice(symbol);
                    MarketType market = "KOSDAQ".equalsIgnoreCase(price.getMarket()) ? MarketType.KOSDAQ : MarketType.KOSPI;
                    Stock newStock = Stock.builder()
                            .symbol(symbol)
                            .name(price.getName())
                            .market(market)
                            .build();
                    return stockRepository.save(newStock);
                });
    }

    @Transactional
    public StockPriceResponse getStockPrice(String symbol) {
        getOrCreateStock(symbol);
        return marketDataClient.fetchCurrentPrice(symbol);
    }

    @Transactional
    public StockCandleResponse getCandles(String symbol, String timeframeCode, LocalDate from, LocalDate to) {
        Timeframe timeframe = Timeframe.from(timeframeCode);
        Stock stock = getOrCreateStock(symbol);

        // 1. 분봉 (Intraday) - DB 비저장 메모리 통과
        if (!timeframe.isPersistent()) {
            List<CandleDto> intradayCandles = marketDataClient.fetchIntradayCandles(symbol, timeframe);
            return StockCandleResponse.builder()
                    .symbol(symbol)
                    .name(stock.getName())
                    .timeframe(timeframe.getCode())
                    .candles(intradayCandles)
                    .build();
        }

        // 2. 일봉/주봉/월봉 (Historical) - 온디맨드 백필 & DB 영구 저장
        stockSyncService.syncDailyCandles(stock);

        LocalDate startDate = (from != null) ? from : LocalDate.now().minusYears(1);
        LocalDate endDate = (to != null) ? to : LocalDate.now();

        List<DailyCandle> dailyCandles = dailyCandleRepository
                .findByStockAndCandleDateBetweenOrderByCandleDateAsc(stock, startDate, endDate);

        List<CandleDto> resultCandles;
        if (timeframe == Timeframe.W1) {
            resultCandles = aggregateWeekly(dailyCandles);
        } else if (timeframe == Timeframe.MN1) {
            resultCandles = aggregateMonthly(dailyCandles);
        } else {
            resultCandles = dailyCandles.stream()
                    .map(c -> CandleDto.builder()
                            .time(c.getCandleDate().toString())
                            .open(c.getOpenPrice())
                            .high(c.getHighPrice())
                            .low(c.getLowPrice())
                            .close(c.getClosePrice())
                            .volume(c.getVolume())
                            .tradingValue(c.getTradingValue())
                            .build())
                    .toList();
        }

        return StockCandleResponse.builder()
                .symbol(symbol)
                .name(stock.getName())
                .timeframe(timeframe.getCode())
                .candles(resultCandles)
                .build();
    }

    private List<CandleDto> aggregateWeekly(List<DailyCandle> dailyCandles) {
        Map<LocalDate, List<DailyCandle>> weeklyGroups = new LinkedHashMap<>();
        for (DailyCandle candle : dailyCandles) {
            LocalDate monday = candle.getCandleDate().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            weeklyGroups.computeIfAbsent(monday, k -> new ArrayList<>()).add(candle);
        }
        return weeklyGroups.entrySet().stream()
                .map(entry -> buildAggregatedCandle(entry.getKey().toString(), entry.getValue()))
                .toList();
    }

    private List<CandleDto> aggregateMonthly(List<DailyCandle> dailyCandles) {
        Map<YearMonth, List<DailyCandle>> monthlyGroups = new LinkedHashMap<>();
        for (DailyCandle candle : dailyCandles) {
            YearMonth ym = YearMonth.from(candle.getCandleDate());
            monthlyGroups.computeIfAbsent(ym, k -> new ArrayList<>()).add(candle);
        }
        return monthlyGroups.entrySet().stream()
                .map(entry -> buildAggregatedCandle(entry.getKey().atDay(1).toString(), entry.getValue()))
                .toList();
    }

    private CandleDto buildAggregatedCandle(String timeLabel, List<DailyCandle> candles) {
        long open = candles.get(0).getOpenPrice();
        long close = candles.get(candles.size() - 1).getClosePrice();
        long high = candles.stream().mapToLong(DailyCandle::getHighPrice).max().orElse(open);
        long low = candles.stream().mapToLong(DailyCandle::getLowPrice).min().orElse(open);
        long volume = candles.stream().mapToLong(DailyCandle::getVolume).sum();
        long tradingValue = candles.stream().mapToLong(c -> c.getTradingValue() != null ? c.getTradingValue() : 0).sum();

        return CandleDto.builder()
                .time(timeLabel)
                .open(open)
                .high(high)
                .low(low)
                .close(close)
                .volume(volume)
                .tradingValue(tradingValue)
                .build();
    }
}
