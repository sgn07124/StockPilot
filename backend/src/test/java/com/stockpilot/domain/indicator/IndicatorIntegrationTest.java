package com.stockpilot.domain.indicator;

import com.stockpilot.domain.indicator.entity.TechnicalIndicator;
import com.stockpilot.domain.indicator.repository.TechnicalIndicatorRepository;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.repository.StockRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class IndicatorIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private TechnicalIndicatorRepository technicalIndicatorRepository;

    @Test
    @DisplayName("보조지표 API 호출 시 지표가 자동 연산되어 DB에 저장되고 최신 요약과 함께 반환된다")
    void getIndicatorsApi() throws Exception {
        // 삼성전자(005930) 보조지표 API 호출
        mockMvc.perform(get("/api/v1/stocks/005930/indicators?timeframe=1D")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.symbol").value("005930"))
                .andExpect(jsonPath("$.data.name").value("삼성전자"))
                .andExpect(jsonPath("$.data.timeframe").value("1D"))
                .andExpect(jsonPath("$.data.indicators").isArray())
                .andExpect(jsonPath("$.data.latestSummary").exists())
                .andExpect(jsonPath("$.data.latestSummary.rsiStatus").exists())
                .andExpect(jsonPath("$.data.latestSummary.signalType").exists());

        // DB에 TechnicalIndicator 엔티티가 실제로 저장되었는지 검증
        Stock stock = stockRepository.findBySymbol("005930").orElseThrow();
        List<TechnicalIndicator> savedIndicators = technicalIndicatorRepository
                .findTopByStockOrderByIndicatorDateDesc(stock).stream().toList();

        assertThat(savedIndicators).isNotEmpty();
    }
}
