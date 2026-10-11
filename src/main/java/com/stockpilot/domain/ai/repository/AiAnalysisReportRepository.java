package com.stockpilot.domain.ai.repository;

import com.stockpilot.domain.ai.entity.AiAnalysisReport;
import com.stockpilot.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface AiAnalysisReportRepository extends JpaRepository<AiAnalysisReport, Long> {

    Optional<AiAnalysisReport> findByStockAndReportDate(Stock stock, LocalDate reportDate);

    Optional<AiAnalysisReport> findTopByStockOrderByReportDateDesc(Stock stock);
}
