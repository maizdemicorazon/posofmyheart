package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "product_variants", indexes = {
        @Index(name = "idx_variant_product", columnList = "id_product"),
        @Index(name = "idx_variant_size", columnList = "size"),
        @Index(name = "idx_variant_sell_price", columnList = "actual_sell_price"),
        @Index(name = "idx_variant_cost_price", columnList = "actual_cost_price")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variant")
    private Long idVariant;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @Column(name = "size", length = 30, nullable = false)
    private String size;

    @Column(name = "actual_sell_price")
    private BigDecimal actualSellPrice;

    @Column(name = "actual_cost_price")
    private BigDecimal actualCostPrice;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "effective_date")
    private LocalDateTime effectiveDate = LocalDateTime.now();
}
