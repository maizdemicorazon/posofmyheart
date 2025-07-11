package com.mdmc.posofmyheart.infrastructure.persistence.entities.products;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductCategoryEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "products", indexes = {
        @Index(name = "idx_product_category", columnList = "id_category"),
        @Index(name = "idx_product_name", columnList = "name"),
        @Index(name = "idx_product_image", columnList = "id_image")
})
@NamedEntityGraphs({
        @NamedEntityGraph(
                name = "Product.withAllRelations",
                attributeNodes = {
                        @NamedAttributeNode("category"),
                        @NamedAttributeNode("variants"),
                        @NamedAttributeNode("flavors")
                }
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_product")
    private Long idProduct;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_category", nullable = false)
    private ProductCategoryEntity category;

    @Column(name = "name", length = 100, nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_image")
    private CatalogImageEntity image;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(nullable = false)
    private boolean active;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductVariantEntity> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductFlavorEntity> flavors;

}