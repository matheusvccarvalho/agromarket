
package com.agromarket.api.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do produto é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    @Column(nullable = false, length = 150)
    private String name;

    @NotBlank(message = "A categoria é obrigatória")
    @Size(max = 80, message = "A categoria deve ter no máximo 80 caracteres")
    @Column(nullable = false, length = 80)
    private String category;

    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres")
    @Column(length = 500)
    private String description;

    @NotBlank(message = "A unidade de medida é obrigatória")
    @Size(max = 20, message = "A unidade deve ter no máximo 20 caracteres")
    @Column(nullable = false, length = 20)
    private String unit;

    @NotNull(message = "O preço é obrigatório")
    @DecimalMin(value = "0.00", message = "O preço não pode ser negativo")
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @NotNull(message = "A quantidade em estoque é obrigatória")
    @DecimalMin(value = "0.000", message = "O estoque não pode ser negativo")
    @Column(name = "stock_quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal stockQuantity = BigDecimal.ZERO;

    @NotNull(message = "O estoque mínimo é obrigatório")
    @DecimalMin(value = "0.000", message = "O estoque mínimo não pode ser negativo")
    @Column(name = "minimum_stock", nullable = false, precision = 12, scale = 3)
    private BigDecimal minimumStock = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getUnit() { return unit; }
    public void setUnit(String unit) { this.unit = unit; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public BigDecimal getMinimumStock() { return minimumStock; }
    public void setMinimumStock(BigDecimal minimumStock) {
        this.minimumStock = minimumStock;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
