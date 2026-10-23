
package com.agromarket.api.repository;

import com.agromarket.api.entity.MarketQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MarketQuoteRepository
        extends JpaRepository<MarketQuote, Long> {

    List<MarketQuote> findByCommodityIgnoreCaseOrderByReferenceDateDesc(
            String commodity
    );

    List<MarketQuote> findByReferenceDateBetweenOrderByReferenceDateDesc(
            LocalDate startDate,
            LocalDate endDate
    );

    List<MarketQuote> findByCommodityIgnoreCaseAndReferenceDateBetweenOrderByReferenceDateAsc(
            String commodity,
            LocalDate startDate,
            LocalDate endDate
    );
}
