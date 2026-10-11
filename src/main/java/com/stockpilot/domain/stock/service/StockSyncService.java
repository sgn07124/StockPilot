package com.stockpilot.domain.stock.service;

import com.stockpilot.domain.stock.dto.CandleDto;
import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.repository.DailyCandleRepository;
import com.stockpilot.domain.stock.repository.StockRepository;
import com.stockpilot.infra.market.MarketDataClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StockSyncService {

    private final StockRepository stockRepository;
    private final DailyCandleRepository dailyCandleRepository;
    private final MarketDataClient marketDataClient;

    /**
     * 일봉 시세 온디맨드 동기화 (Lazy Backfill & Delta Sync)
     */
    @Transactional
    public void syncDailyCandles(Stock stock) {
        LocalDate latestDate = stock.getLatestCandleDate();
        LocalDate today = LocalDate.now();

        if (latestDate == null) {
            // 1. Cold Cache: 최근 1년 치 시세 최초 백필 (Backfill)
            LocalDate oneYearAgo = today.minusYears(1);
            log.info("[Sync] Cold Start: Backfilling 1 year candles for {} ({} ~ {})",
                    stock.getSymbol(), oneYearAgo, today);

            List<CandleDto> candleDtos = marketDataClient.fetchDailyCandles(stock.getSymbol(), oneYearAgo, today);
            saveCandles(stock, candleDtos);
        } else if (latestDate.isBefore(today)) {
            // 2. Outdated Cache: 누락된 일자만 추가 동기화 (Delta Sync)
            LocalDate nextDay = latestDate.plusDays(1);
            if (!nextDay.isAfter(today)) {
                log.info("[Sync] Delta Sync: Fetching missing candles for {} ({} ~ {})",
                        stock.getSymbol(), nextDay, today);

                List<CandleDto> deltaDtos = marketDataClient.fetchDailyCandles(stock.getSymbol(), nextDay, today);
                saveCandles(stock, deltaDtos);
            }
        } else {
            // 3. Cache Hit: 오늘 날짜까지 이미 최신화됨 (외부 API 호출 없음)
            log.debug("[Sync] Cache Hit: Stock {} already up to date ({})", stock.getSymbol(), latestDate);
        }
    }

    private void saveCandles(Stock stock, List<CandleDto> dtos) {
        if (dtos == null || dtos.isEmpty()) {
            return;
        }

        List<DailyCandle> entities = dtos.stream()
                .map(dto -> DailyCandle.builder()
                        .stock(stock)
                        .candleDate(LocalDate.parse(dto.getTime()))
                        .openPrice(dto.getOpen())
                        .highPrice(dto.getHigh())
                        .lowPrice(dto.getLow())
                        .closePrice(dto.getClose())
                        .volume(dto.getVolume())
                        .tradingValue(dto.getTradingValue())
                        .build())
                .toList();

        dailyCandleRepository.saveAll(entities);

        // 최신 일봉 일자로 업데이트
        LocalDate maxDate = entities.stream()
                .map(DailyCandle::getCandleDate)
                .max(LocalDate::compareTo)
                .orElse(stock.getLatestCandleDate());

        if (maxDate != null && (stock.getLatestCandleDate() == null || maxDate.isAfter(stock.getLatestCandleDate()))) {
            stock.updateLatestCandleDate(maxDate);
            stockRepository.save(stock);
        }

        log.info("[Sync] Successfully saved {} daily candles for {}, latest date: {}",
                entities.size(), stock.getSymbol(), stock.getLatestCandleDate());
    }
}
