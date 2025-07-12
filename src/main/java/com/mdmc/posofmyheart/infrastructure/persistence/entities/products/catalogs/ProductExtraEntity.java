package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

@Entity
@Table(name = "product_extras", indexes = {
        @Index(name = "idx_extra_name", columnList = "name"),
        @Index(name = "idx_extra_active", columnList = "active"),
        @Index(name = "idx_extra_price", columnList = "actual_price"),
        @Index(name = "idx_extra_cost", columnList = "actual_cost"),
        @Index(name = "idx_extra_image", columnList = "id_image")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductExtraEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_extra")
    private Long idExtra;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "actual_price", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualPrice;

    @Column(name = "actual_cost", precision = 10, scale = 2, nullable = false)
    private BigDecimal actualCost;

    @Column(name = "active")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_image")
    private CatalogImageEntity image;

    @OneToMany(mappedBy = "productExtra", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<OrderExtraDetailEntity> extraDetails = new HashSet<>();

    public ProductExtraEntity(Long idExtra, String name, String description, BigDecimal actualPrice,
                              BigDecimal actualCost, CatalogImageEntity image) {
        this.idExtra = idExtra;
        this.name = name;
        this.description = description;
        this.actualPrice = actualPrice;
        this.actualCost = actualCost;
        this.image = image;
    }
}
