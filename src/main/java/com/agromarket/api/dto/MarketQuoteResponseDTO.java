
package com.agromarket.api.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record MarketQuoteResponseDTO(
    Long id,
    String commodity,
    String region,
    String state,
    BigDecimal price,
    String unit,
    LocalDate referenceDate,
    String source,
    String dataType,
    LocalDateTime createdAt
) {}
