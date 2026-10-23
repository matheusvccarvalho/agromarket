
package com.agromarket.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;

public record MarketQuoteRequestDTO(

    @NotBlank(message = "A commodity é obrigatória")
    @Size(max = 100)
    String commodity,

    @NotBlank(message = "A região é obrigatória")
    @Size(max = 150)
    String region,

    @Size(min = 2, max = 2, message = "Informe a UF com duas letras")
    String state,

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    BigDecimal price,

    @NotBlank(message = "A unidade é obrigatória")
    @Size(max = 30)
    String unit,

    @NotNull(message = "A data de referência é obrigatória")
    LocalDate referenceDate,

    @NotBlank(message = "A fonte é obrigatória")
    @Size(max = 150)
    String source
) {}
