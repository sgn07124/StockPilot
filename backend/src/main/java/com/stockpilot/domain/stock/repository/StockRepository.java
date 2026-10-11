package com.stockpilot.domain.stock.repository;

import com.stockpilot.domain.stock.entity.MarketType;
import com.stockpilot.domain.stock.entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, String> {

    Optional<Stock> findBySymbol(String symbol);

    List<Stock> findByMarket(MarketType market);

    List<Stock> findByNameContaining(String keyword);
}
