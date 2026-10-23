
package com.agromarket.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceAnalyticsResponseDTO(
        String commodity,
        LocalDate startDate,
        LocalDate endDate,
        long quantity,
        BigDecimal averagePrice,
        BigDecimal minimumPrice,
        BigDecimal maximumPrice
) {
}
