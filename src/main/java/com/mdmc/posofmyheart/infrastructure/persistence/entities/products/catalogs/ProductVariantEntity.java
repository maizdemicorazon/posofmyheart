package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import java.math.BigDecimal;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
public class ProductVariantEntity extends BaseEntity {
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
}
