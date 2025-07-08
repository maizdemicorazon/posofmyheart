package com.mdmc.posofmyheart.infrastructure.persistence.entities.products;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Entity
@Table(name = "product_flavors", indexes = {
        @Index(name = "idx_flavors_id_product", columnList = "id_product")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFlavorEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idFlavor;
    private String name;
    @ColumnDefault("true")
    @Column(nullable = false)
    private boolean active = true;
    @Column(name = "image", nullable = false)
    private byte[] image;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    public ProductFlavorEntity(Long idFlavor, String name, ProductEntity product) {
        this.idFlavor = idFlavor;
        this.name = name;
        this.product = product;
    }
}