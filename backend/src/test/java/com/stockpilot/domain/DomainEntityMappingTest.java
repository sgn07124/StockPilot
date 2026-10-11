package com.stockpilot.domain;

import com.stockpilot.domain.ai.entity.AiAnalysisReport;
import com.stockpilot.domain.ai.entity.TrendType;
import com.stockpilot.domain.ai.repository.AiAnalysisReportRepository;
import com.stockpilot.domain.indicator.entity.SignalType;
import com.stockpilot.domain.indicator.entity.TechnicalIndicator;
import com.stockpilot.domain.indicator.repository.TechnicalIndicatorRepository;
import com.stockpilot.domain.member.entity.Member;
import com.stockpilot.domain.member.entity.Role;
import com.stockpilot.domain.member.repository.MemberRepository;
import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.MarketType;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.stock.repository.DailyCandleRepository;
import com.stockpilot.domain.stock.repository.StockRepository;
import com.stockpilot.domain.watchlist.entity.Watchlist;
import com.stockpilot.domain.watchlist.repository.WatchlistRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.context.annotation.Import;
import com.stockpilot.common.config.JpaConfig;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@Import(JpaConfig.class)
class DomainEntityMappingTest {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private StockRepository stockRepository;

    @Autowired
    private DailyCandleRepository dailyCandleRepository;

    @Autowired
    private TechnicalIndicatorRepository technicalIndicatorRepository;

    @Autowired
    private AiAnalysisReportRepository aiAnalysisReportRepository;

    @Autowired
    private WatchlistRepository watchlistRepository;

    @Test
    @DisplayName("Member 엔티티 저장 및 조회와 BaseTimeEntity 동작을 검증한다")
    void memberEntityTest() {
        Member member = Member.builder()
                .email("user@stockpilot.com")
                .password("encoded_secret_pwd")
                .nickname("차트고수")
                .role(Role.ROLE_USER)
                .build();

        Member saved = memberRepository.save(member);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
        assertThat(memberRepository.findByEmail("user@stockpilot.com")).isPresent();
        assertThat(memberRepository.existsByEmail("user@stockpilot.com")).isTrue();
    }

    @Test
    @DisplayName("Stock 및 DailyCandle 엔티티 연관관계 매핑 및 조회를 검증한다")
    void stockAndDailyCandleTest() {
        Stock stock = Stock.builder()
                .symbol("005930")
                .name("삼성전자")
                .market(MarketType.KOSPI)
                .latestCandleDate(LocalDate.of(2026, 10, 10))
                .build();
        stockRepository.save(stock);

        DailyCandle candle = DailyCandle.builder()
                .stock(stock)
                .candleDate(LocalDate.of(2026, 10, 10))
                .openPrice(72000L)
                .highPrice(73500L)
                .lowPrice(71800L)
                .closePrice(73000L)
                .volume(15000000L)
                .tradingValue(1095000000000L)
                .build();
        dailyCandleRepository.save(candle);

        List<DailyCandle> candles = dailyCandleRepository.findByStockAndCandleDateBetweenOrderByCandleDateAsc(
                stock, LocalDate.of(2026, 10, 1), LocalDate.of(2026, 10, 11));

        assertThat(candles).hasSize(1);
        assertThat(candles.get(0).getStock().getSymbol()).isEqualTo("005930");
        assertThat(candles.get(0).getClosePrice()).isEqualTo(73000L);
    }

    @Test
    @DisplayName("TechnicalIndicator 엔티티 저장 및 최신 지표 조회를 검증한다")
    void technicalIndicatorTest() {
        Stock stock = stockRepository.save(Stock.builder()
                .symbol("000660")
                .name("SK하이닉스")
                .market(MarketType.KOSPI)
                .build());

        TechnicalIndicator indicator = TechnicalIndicator.builder()
                .stock(stock)
                .indicatorDate(LocalDate.of(2026, 10, 10))
                .sma5(new BigDecimal("185000.00"))
                .sma20(new BigDecimal("180000.00"))
                .sma60(new BigDecimal("175000.00"))
                .sma120(new BigDecimal("160000.00"))
                .rsi14(new BigDecimal("68.50"))
                .signalType(SignalType.GOLDEN_CROSS)
                .build();
        technicalIndicatorRepository.save(indicator);

        Optional<TechnicalIndicator> latest = technicalIndicatorRepository.findTopByStockOrderByIndicatorDateDesc(stock);
        assertThat(latest).isPresent();
        assertThat(latest.get().getSignalType()).isEqualTo(SignalType.GOLDEN_CROSS);
        assertThat(latest.get().getRsi14()).isEqualTo(new BigDecimal("68.50"));
    }

    @Test
    @DisplayName("AiAnalysisReport 엔티티 저장 및 최신 리포트 조회를 검증한다")
    void aiReportTest() {
        Stock stock = stockRepository.save(Stock.builder()
                .symbol("035420")
                .name("NAVER")
                .market(MarketType.KOSPI)
                .build());

        AiAnalysisReport report = AiAnalysisReport.builder()
                .stock(stock)
                .reportDate(LocalDate.of(2026, 10, 10))
                .trend(TrendType.BULLISH)
                .rsiStatus("과매도 탈출 구간")
                .maSignal("5일선-20일선 골든크로스")
                .summary("단기 바닥을 다지고 기술적 반등 흐름이 뚜렷합니다.")
                .riskFactors("[\"직전 고점 매물대 저항\"]")
                .build();
        aiAnalysisReportRepository.save(report);

        Optional<AiAnalysisReport> found = aiAnalysisReportRepository.findByStockAndReportDate(
                stock, LocalDate.of(2026, 10, 10));
        assertThat(found).isPresent();
        assertThat(found.get().getTrend()).isEqualTo(TrendType.BULLISH);
    }

    @Test
    @DisplayName("Watchlist 관심종목 등록, 조회, 삭제를 검증한다")
    void watchlistTest() {
        Member member = memberRepository.save(Member.builder()
                .email("pilot@test.com")
                .password("pwd")
                .nickname("파일럿")
                .build());

        Stock stock = stockRepository.save(Stock.builder()
                .symbol("005930")
                .name("삼성전자")
                .market(MarketType.KOSPI)
                .build());

        Watchlist watchlist = Watchlist.builder()
                .member(member)
                .stock(stock)
                .build();
        watchlistRepository.save(watchlist);

        assertThat(watchlistRepository.existsByMemberAndStock(member, stock)).isTrue();
        List<Watchlist> list = watchlistRepository.findByMemberOrderByCreatedAtDesc(member);
        assertThat(list).hasSize(1);
        assertThat(list.get(0).getStock().getName()).isEqualTo("삼성전자");

        watchlistRepository.deleteByMemberAndStock(member, stock);
        assertThat(watchlistRepository.existsByMemberAndStock(member, stock)).isFalse();
    }
}
