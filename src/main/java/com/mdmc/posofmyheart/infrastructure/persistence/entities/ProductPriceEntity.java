package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_prices")
@Getter @Setter @NoArgsConstructor
public class ProductPriceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_price")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @Column(name = "sell_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal sellPrice;

    @Column(name = "cost_price", nullable = false, precision = 10, scale = 2)
    private BigDecimal costPrice;

    @Column(name = "effective_date")
    private LocalDateTime effectiveDate = LocalDateTime.now();
}