package com.stockpilot.domain.indicator.repository;

import com.stockpilot.domain.indicator.entity.TechnicalIndicator;
import com.stockpilot.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface TechnicalIndicatorRepository extends JpaRepository<TechnicalIndicator, Long> {

    Optional<TechnicalIndicator> findByStockAndIndicatorDate(Stock stock, LocalDate indicatorDate);

    Optional<TechnicalIndicator> findTopByStockOrderByIndicatorDateDesc(Stock stock);

    List<TechnicalIndicator> findByStockAndIndicatorDateBetweenOrderByIndicatorDateAsc(
            Stock stock, LocalDate startDate, LocalDate endDate);
}
