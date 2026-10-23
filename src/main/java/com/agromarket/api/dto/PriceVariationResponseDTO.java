
package com.agromarket.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PriceVariationResponseDTO(
        String commodity,
        String unit,
        LocalDate startDate,
        LocalDate endDate,
        LocalDate initialDate,
        LocalDate finalDate,
        BigDecimal initialPrice,
        BigDecimal finalPrice,
        BigDecimal absoluteVariation,
        BigDecimal percentageVariation,
        String trend
) {
}
