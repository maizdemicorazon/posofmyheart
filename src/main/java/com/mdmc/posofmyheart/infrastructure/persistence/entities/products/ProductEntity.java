package com.mdmc.posofmyheart.infrastructure.persistence.entities.products;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductCategoryEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
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
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedEntityGraphs;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
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
                        @NamedAttributeNode("flavors"),
                        @NamedAttributeNode("image")
                }
        )
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {

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
    private boolean active = true;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ColumnDefault(value = "CURRENT_TIMESTAMP")
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductVariantEntity> variants;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<ProductFlavorEntity> flavors;

    @PrePersist
    protected void onCreate() {
        if (variants == null) {
            variants = new HashSet<>();
        }
        if (flavors == null) {
            flavors = new HashSet<>();
        }
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Método de conveniencia para obtener los datos de imagen
    public byte[] getImageData() {
        return image != null && image.isActive() ? image.getImageDataSafe() : new byte[0];
    }

    // Método para verificar si tiene imagen
    public boolean hasImage() {
        return image != null && image.isActive();
    }
}