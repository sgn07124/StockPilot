package com.stockpilot.domain.ai.entity;

import com.stockpilot.domain.stock.entity.Stock;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "ai_analysis_report",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_ai_report_symbol_date", columnNames = {"stock_symbol", "report_date"})
        },
        indexes = {
                @Index(name = "idx_ai_report_lookup", columnList = "stock_symbol, report_date DESC")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class AiAnalysisReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_symbol", nullable = false)
    private Stock stock;

    @Column(name = "report_date", nullable = false)
    private LocalDate reportDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TrendType trend;

    @Column(name = "rsi_status", nullable = false, length = 100)
    private String rsiStatus;

    @Column(name = "ma_signal", nullable = false, length = 100)
    private String maSignal;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(name = "risk_factors", columnDefinition = "TEXT")
    private String riskFactors;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
