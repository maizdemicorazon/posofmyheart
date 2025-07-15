package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

import com.mdmc.posofmyheart.infrastructure.persistence.entities.AuditEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

@Entity
@Table(name = "basics", indexes = {
        @Index(name = "idx_basics_id_basic", columnList = "id_basic"),
        @Index(name = "idx_basics_name", columnList = "name"),
        @Index(name = "idx_basics_active", columnList = "active"),
        @Index(name = "idx_basics_image", columnList = "id_image")
})
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
public class ProductBasicEntity extends AuditEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long idBasic;
    @Column
    private String name;
    @Column
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_image")
    private CatalogImageEntity image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;
}
