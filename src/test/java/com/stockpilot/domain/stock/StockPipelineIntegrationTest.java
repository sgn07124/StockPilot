package com.stockpilot.domain.stock;

import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.repository.DailyCandleRepository;
import com.stockpilot.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class StockPipelineIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DailyCandleRepository dailyCandleRepository;

    @Test
    @DisplayName("지원 종목 목록 API를 호출하면 기본 종목 목록을 반환한다")
    void getSupportedStocks() throws Exception {
        mockMvc.perform(get("/api/v1/stocks")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray())
                .andExpect(jsonPath("$.data[0].symbol").value("005930"))
                .andExpect(jsonPath("$.data[0].name").value("삼성전자"));
    }

    @Test
    @DisplayName("특정 종목의 현재가 시세를 조회한다")
    void getStockPrice() throws Exception {
        mockMvc.perform(get("/api/v1/stocks/005930")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.symbol").value("005930"))
                .andExpect(jsonPath("$.data.name").value("삼성전자"))
                .andExpect(jsonPath("$.data.currentPrice").isNumber())
                .andExpect(jsonPath("$.data.volume").isNumber());
    }

    @Test
    @DisplayName("최초 일봉 조회 시 최근 1년 치 시세가 DB에 온디맨드로 백필된다")
    void lazyBackfillDailyCandles() throws Exception {
        // 1. API 호출 전 삼성전자 캔들 존재 여부 확인
        Stock stock = stockRepository.findBySymbol("005930").orElseThrow();

        // 2. 일봉 차트 API 호출 (timeframe=1D)
        mockMvc.perform(get("/api/v1/stocks/005930/candles?timeframe=1D")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.symbol").value("005930"))
                .andExpect(jsonPath("$.data.timeframe").value("1D"))
                .andExpect(jsonPath("$.data.candles").isArray());

        // 3. 호출 후 DB에 백필되었는지 검증
        List<DailyCandle> savedCandles = dailyCandleRepository.findByStockOrderByCandleDateDesc(stock);
        assertThat(savedCandles).isNotEmpty();
        assertThat(savedCandles.size()).isGreaterThan(200); // 1년 평일 수치 대략 250개

        Stock updatedStock = stockRepository.findBySymbol("005930").orElseThrow();
        assertThat(updatedStock.getLatestCandleDate()).isNotNull();
    }

    @Test
    @DisplayName("분봉(5m) 조회 시 DB에 저장되지 않고 실시간 메모리 통과로 반환된다")
    void intradayMinutesCandles() throws Exception {
        // SK하이닉스(000660) 초기 일봉 개수 확인
        Stock stock = stockRepository.findBySymbol("000660").orElseThrow();
        int initialCandleCount = dailyCandleRepository.findByStockOrderByCandleDateDesc(stock).size();

        // 5분봉 조회 API 호출
        mockMvc.perform(get("/api/v1/stocks/000660/candles?timeframe=5m")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.symbol").value("000660"))
                .andExpect(jsonPath("$.data.timeframe").value("5m"))
                .andExpect(jsonPath("$.data.candles").isArray());

        // 분봉 조회 후에도 DB의 daily_candle 개수는 증가하지 않아야 함 (No Persistence)
        int afterCandleCount = dailyCandleRepository.findByStockOrderByCandleDateDesc(stock).size();
        assertThat(afterCandleCount).isEqualTo(initialCandleCount);
    }

    @Test
    @DisplayName("유효하지 않은 timeframe 파라미터 전달 시 400 에러와 S002 코드를 반환한다")
    void invalidTimeframe() throws Exception {
        mockMvc.perform(get("/api/v1/stocks/005930/candles?timeframe=999x")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.error.code").value("S002"));
    }
}
