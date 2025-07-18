package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import org.hibernate.annotations.ColumnDefault;

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
@Table(name = "product_flavors", indexes = {
        @Index(name = "idx_flavors_id_product", columnList = "id_product"),
        @Index(name = "idx_flavors_name", columnList = "name"),
        @Index(name = "idx_flavors_active", columnList = "active"),
        @Index(name = "idx_flavors_image", columnList = "id_image")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFlavorEntity extends AuditEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlavor;

    private String name;

    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean active = true;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_image")
    private CatalogImageEntity image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

}
