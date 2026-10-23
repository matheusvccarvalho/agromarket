
package com.agromarket.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "market_quotes")
public class MarketQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "A commodity é obrigatória")
    @Size(max = 100)
    @Column(nullable = false, length = 100)
    private String commodity;

    @NotBlank(message = "A região é obrigatória")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String region;

    @Size(min = 2, max = 2, message = "Informe a UF com duas letras")
    @Column(length = 2)
    private String state;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotBlank(message = "A unidade é obrigatória")
    @Size(max = 30)
    @Column(nullable = false, length = 30)
    private String unit;

    @NotNull(message = "A data de referência é obrigatória")
    @Column(name = "reference_date", nullable = false)
    private LocalDate referenceDate;

    @NotBlank(message = "A fonte é obrigatória")
    @Size(max = 150)
    @Column(nullable = false, length = 150)
    private String source;

    @NotBlank
    @Column(name = "data_type", nullable = false, length = 20)
    private String dataType = "MANUAL";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (dataType != null) {
            dataType = dataType.toUpperCase();
        }
        if (state != null) {
            state = state.toUpperCase();
        }
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCommodity() { return commodity; }
    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }

    public String getState() { return state; }
    public void setState(String state) { this.state = state; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public LocalDate getReferenceDate() { return referenceDate; }
    public void setReferenceDate(LocalDate referenceDate) {
        this.referenceDate = referenceDate;
    }

    public String getSource() { return source; }
    public void setSource(String source) { this.source = source; }

    public String getDataType() { return dataType; }
    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
}
