
package com.agromarket.api.dto;

import com.agromarket.api.entity.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProductResponseDTO(
    Long id,
    String name,
    String category,
    String description,
    String unit,
    BigDecimal price,
    BigDecimal stockQuantity,
    BigDecimal minimumStock,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
    public static ProductResponseDTO fromEntity(Product product) {
        return new ProductResponseDTO(
            product.getId(),
            product.getName(),
            product.getCategory(),
            product.getDescription(),
            product.getUnit(),
            product.getPrice(),
            product.getStockQuantity(),
            product.getMinimumStock(),
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
