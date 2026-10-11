package com.stockpilot.domain.watchlist.repository;

import com.stockpilot.domain.member.entity.Member;
import com.stockpilot.domain.stock.entity.Stock;
import com.stockpilot.domain.watchlist.entity.Watchlist;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WatchlistRepository extends JpaRepository<Watchlist, Long> {

    List<Watchlist> findByMemberOrderByCreatedAtDesc(Member member);

    Optional<Watchlist> findByMemberAndStock(Member member, Stock stock);

    boolean existsByMemberAndStock(Member member, Stock stock);

    void deleteByMemberAndStock(Member member, Stock stock);
}
