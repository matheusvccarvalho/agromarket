
package com.agromarket.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record ProductRequestDTO(
    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    String name,

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 80, message = "A categoria deve ter no máximo 80 caracteres")
    String category,

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    String description,

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 20, message = "A unidade deve ter no máximo 20 caracteres")
    String unit,

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    BigDecimal price,

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @DecimalMin(value = "0.000", message = "O estoque não pode ser negativo")
    BigDecimal stockQuantity,

    @NotNull(message = "O estoque mínimo é obrigatório")
    @DecimalMin(value = "0.000", message = "O estoque mínimo não pode ser negativo")
    BigDecimal minimumStock
) {}
