package com.stockpilot.infra.market.kis;

import com.stockpilot.domain.stock.dto.CandleDto;
import com.stockpilot.domain.stock.dto.StockPriceResponse;
import com.stockpilot.domain.stock.entity.Timeframe;
import com.stockpilot.infra.market.MarketDataClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Component("kisMarketDataClient")
@ConditionalOnProperty(name = "kis.api.enabled", havingValue = "true")
public class KisMarketDataClient implements MarketDataClient {

    @Value("${kis.api.base-url}")
    private String baseUrl;

    @Value("${kis.api.app-key}")
    private String appKey;

    @Value("${kis.api.app-secret}")
    private String appSecret;

    @Override
    public StockPriceResponse fetchCurrentPrice(String symbol) {
        log.info("[KIS API] Fetching current price for {} from {}", symbol, baseUrl);
        // 실제 KIS API 연동 시 RestClient 호출 로직 구현 예정
        throw new UnsupportedOperationException("KIS API 실전 연동 준비 중입니다.");
    }

    @Override
    public List<CandleDto> fetchDailyCandles(String symbol, LocalDate from, LocalDate to) {
        log.info("[KIS API] Fetching daily candles for {} from {} to {}", symbol, from, to);
        throw new UnsupportedOperationException("KIS API 실전 연동 준비 중입니다.");
    }

    @Override
    public List<CandleDto> fetchIntradayCandles(String symbol, Timeframe timeframe) {
        log.info("[KIS API] Fetching intraday candles for {} ({})", symbol, timeframe.getCode());
        throw new UnsupportedOperationException("KIS API 실전 연동 준비 중입니다.");
    }
}
