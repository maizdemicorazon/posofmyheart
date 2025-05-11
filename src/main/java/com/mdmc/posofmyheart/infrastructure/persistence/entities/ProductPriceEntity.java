package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_prices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_price")
    private Integer idPrice;

    @ManyToOne
    @JoinColumn(name = "id_variant", nullable = false)
    private ProductVariantEntity variant;

    @Column(name = "sell_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal sellPrice;

    @Column(name = "cost_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal costPrice;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate = LocalDateTime.now();
}
