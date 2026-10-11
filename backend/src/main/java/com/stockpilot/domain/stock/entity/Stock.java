package com.stockpilot.domain.stock.entity;

import com.stockpilot.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "stock")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class Stock extends BaseTimeEntity {

    @Id
    @Column(length = 10)
    private String symbol;

    @Column(nullable = false, length = 100)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private MarketType market;

    @Column(name = "latest_candle_date")
    private LocalDate latestCandleDate;

    public void updateLatestCandleDate(LocalDate latestCandleDate) {
        this.latestCandleDate = latestCandleDate;
    }
}
