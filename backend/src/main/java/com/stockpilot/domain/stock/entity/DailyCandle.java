package com.stockpilot.domain.stock.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
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
        name = "daily_candle",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_candle_symbol_date", columnNames = {"stock_symbol", "candle_date"})
        },
        indexes = {
                @Index(name = "idx_candle_lookup", columnList = "stock_symbol, candle_date DESC")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@EntityListeners(AuditingEntityListener.class)
public class DailyCandle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "stock_symbol", nullable = false)
    private Stock stock;

    @Column(name = "candle_date", nullable = false)
    private LocalDate candleDate;

    @Column(name = "open_price", nullable = false)
    private Long openPrice;

    @Column(name = "high_price", nullable = false)
    private Long highPrice;

    @Column(name = "low_price", nullable = false)
    private Long lowPrice;

    @Column(name = "close_price", nullable = false)
    private Long closePrice;

    @Column(nullable = false)
    private Long volume;

    @Column(name = "trading_value")
    private Long tradingValue;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
}
