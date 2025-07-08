package com.mdmc.posofmyheart.infrastructure.persistence.entities.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "products", indexes = {
        @Index(name = "idx_product_category", columnList = "id_category"),
        @Index(name = "idx_product_name", columnList = "name")
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

    @Column(name = "image", nullable = false)
    private byte[] image;

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
}