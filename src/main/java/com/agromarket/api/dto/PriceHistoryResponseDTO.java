package com.agromarket.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record PriceHistoryResponseDTO(
        String commodity,
        String unit,
        LocalDate startDate,
        LocalDate endDate,
        List<PriceHistoryPointDTO> history
) {
    public record PriceHistoryPointDTO(
            LocalDate date,
            long quantity,
            BigDecimal averagePrice,
            BigDecimal minimumPrice,
            BigDecimal maximumPrice
    ) {
    }
}
