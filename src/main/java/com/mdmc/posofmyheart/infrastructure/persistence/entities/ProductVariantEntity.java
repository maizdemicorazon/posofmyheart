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
@Table(name = "product_variants")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductVariantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_variant")
    private Long idVariant;

    @ManyToOne
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    @Column(name = "size", length = 30, nullable = false)
    private String size;

    @Column(name = "sell_price")
    private BigDecimal sellPrice;

    @Column(name = "cost_price")
    private BigDecimal costPrice;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "effective_date")
    private LocalDateTime effectiveDate = LocalDateTime.now();;
}
