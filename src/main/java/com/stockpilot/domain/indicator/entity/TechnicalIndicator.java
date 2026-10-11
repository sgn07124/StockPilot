package com.stockpilot.domain.indicator.entity;

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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "technical_indicator",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_indicator_symbol_date", columnNames = {"stock_symbol", "indicator_date"})
        },
        indexes = {
                @Index(name = "idx_indicator_lookup", columnList = "stock_symbol, indicator_date DESC")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class TechnicalIndicator {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_symbol", nullable = false)
    private Stock stock;

    @Column(name = "indicator_date", nullable = false)
    private LocalDate indicatorDate;

    @Column(name = "sma_5", precision = 12, scale = 2)
    private BigDecimal sma5;

    @Column(name = "sma_20", precision = 12, scale = 2)
    private BigDecimal sma20;

    @Column(name = "sma_60", precision = 12, scale = 2)
    private BigDecimal sma60;

    @Column(name = "sma_120", precision = 12, scale = 2)
    private BigDecimal sma120;

    @Column(name = "rsi_14", precision = 5, scale = 2)
    private BigDecimal rsi14;

    @Enumerated(EnumType.STRING)
    @Column(name = "signal_type", nullable = false, length = 20)
    @Builder.Default
    private SignalType signalType = SignalType.NONE;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
