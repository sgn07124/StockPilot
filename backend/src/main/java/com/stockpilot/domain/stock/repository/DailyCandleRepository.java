package com.stockpilot.domain.stock.repository;

import com.stockpilot.domain.stock.entity.DailyCandle;
import com.stockpilot.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyCandleRepository extends JpaRepository<DailyCandle, Long> {

    List<DailyCandle> findByStockAndCandleDateBetweenOrderByCandleDateAsc(
            Stock stock, LocalDate startDate, LocalDate endDate);

    List<DailyCandle> findByStockOrderByCandleDateDesc(Stock stock);

    Optional<DailyCandle> findByStockAndCandleDate(Stock stock, LocalDate candleDate);

    Optional<DailyCandle> findTopByStockOrderByCandleDateDesc(Stock stock);
}
