package com.stockpilot.domain.indicator.service;

import com.stockpilot.domain.indicator.dto.IndicatorItemDto;
import com.stockpilot.domain.indicator.dto.IndicatorResponse;
import com.stockpilot.domain.indicator.entity.SignalType;
import com.stockpilot.domain.indicator.entity.TechnicalIndicator;
import com.stockpilot.domain.indicator.repository.TechnicalIndicatorRepository;
import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.repository.DailyCandleRepository;
import com.stockpilot.domain.stock.service.StockService;
import com.stockpilot.domain.stock.service.StockSyncService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class IndicatorService {

    private final StockService stockService;
    private final StockSyncService stockSyncService;
    private final DailyCandleRepository dailyCandleRepository;
    private final TechnicalIndicatorRepository technicalIndicatorRepository;
    private final IndicatorCalculationService indicatorCalculationService;

    /**
     * 특정 종목의 보조지표 조회 및 온디맨드 연산 동기화
     */
    @Transactional
    public IndicatorResponse getIndicators(String symbol, String timeframeCode) {
        Stock stock = stockService.getOrCreateStock(symbol);

        // 1. 캔들 시세 최신화 보장
        stockSyncService.syncDailyCandles(stock);

        // 2. 지표 연산 및 DB 동기화
        syncIndicators(stock);

        // 3. 지표 데이터 조회 (최근 1년 치)
        LocalDate startDate = LocalDate.now().minusYears(1);
        LocalDate endDate = LocalDate.now();

        List<TechnicalIndicator> indicatorList = technicalIndicatorRepository
                .findByStockAndIndicatorDateBetweenOrderByIndicatorDateAsc(stock, startDate, endDate);

        List<IndicatorItemDto> itemDtos = indicatorList.stream()
                .map(this::toDto)
                .toList();

        // 4. 최신 지표 서머리 요약 생성
        Optional<TechnicalIndicator> latestOpt = indicatorList.isEmpty()
                ? Optional.empty()
                : Optional.of(indicatorList.get(indicatorList.size() - 1));

        IndicatorResponse.IndicatorSummaryDto summary = latestOpt.map(this::buildSummary).orElse(null);

        return IndicatorResponse.builder()
                .symbol(symbol)
                .name(stock.getName())
                .timeframe(timeframeCode != null ? timeframeCode : "1D")
                .indicators(itemDtos)
                .latestSummary(summary)
                .build();
    }

    /**
     * DB의 일봉 데이터를 바탕으로 지표 연산 후 technical_indicator 테이블에 저장
     */
    @Transactional
    public void syncIndicators(Stock stock) {
        Optional<TechnicalIndicator> latestIndicatorOpt = technicalIndicatorRepository
                .findTopByStockOrderByIndicatorDateDesc(stock);

        LocalDate latestCandleDate = stock.getLatestCandleDate();
        if (latestCandleDate == null) {
            return;
        }

        // 이미 최신 캔들까지 지표가 계산되어 있다면 건너뜀
        if (latestIndicatorOpt.isPresent() && !latestIndicatorOpt.get().getIndicatorDate().isBefore(latestCandleDate)) {
            log.debug("[IndicatorSync] Indicators already up to date for {}", stock.getSymbol());
            return;
        }

        log.info("[IndicatorSync] Calculating technical indicators for {}", stock.getSymbol());
        List<DailyCandle> allCandles = dailyCandleRepository
                .findByStockAndCandleDateBetweenOrderByCandleDateAsc(stock, LocalDate.of(2000, 1, 1), LocalDate.now());

        if (allCandles.isEmpty()) {
            return;
        }

        List<TechnicalIndicator> calculatedList = indicatorCalculationService.calculateIndicators(stock, allCandles);

        if (latestIndicatorOpt.isEmpty()) {
            // 최초 연산: 전체 저장
            technicalIndicatorRepository.saveAll(calculatedList);
        } else {
            // 신규 일자만 필터링하여 저장
            LocalDate lastCalculated = latestIndicatorOpt.get().getIndicatorDate();
            List<TechnicalIndicator> deltaList = calculatedList.stream()
                    .filter(i -> i.getIndicatorDate().isAfter(lastCalculated))
                    .toList();
            technicalIndicatorRepository.saveAll(deltaList);
        }

        log.info("[IndicatorSync] Successfully synced indicators for {}", stock.getSymbol());
    }

    private IndicatorItemDto toDto(TechnicalIndicator indicator) {
        return IndicatorItemDto.builder()
                .time(indicator.getIndicatorDate().toString())
                .sma5(indicator.getSma5())
                .sma20(indicator.getSma20())
                .sma60(indicator.getSma60())
                .sma120(indicator.getSma120())
                .rsi14(indicator.getRsi14())
                .signalType(indicator.getSignalType().name())
                .build();
    }

    private IndicatorResponse.IndicatorSummaryDto buildSummary(TechnicalIndicator latest) {
        String rsiStatus = "중립 구간";
        if (latest.getRsi14() != null) {
            if (latest.getRsi14().compareTo(BigDecimal.valueOf(70)) >= 0) {
                rsiStatus = "과매수 (Overbought - 차익실현 유의)";
            } else if (latest.getRsi14().compareTo(BigDecimal.valueOf(30)) <= 0) {
                rsiStatus = "과매도 (Oversold - 반등 가능성)";
            }
        }

        String signalDesc = "특이 시그널 없음";
        if (latest.getSignalType() == SignalType.GOLDEN_CROSS) {
            signalDesc = "5일선이 20일선을 상향 돌파 (골든크로스)";
        } else if (latest.getSignalType() == SignalType.DEAD_CROSS) {
            signalDesc = "5일선이 20일선을 하향 돌파 (데드크로스)";
        }

        return IndicatorResponse.IndicatorSummaryDto.builder()
                .latestDate(latest.getIndicatorDate().toString())
                .sma5(latest.getSma5())
                .sma20(latest.getSma20())
                .sma60(latest.getSma60())
                .sma120(latest.getSma120())
                .rsi14(latest.getRsi14())
                .rsiStatus(rsiStatus)
                .signalType(latest.getSignalType().name())
                .signalDescription(signalDesc)
                .build();
    }
}
