package com.mdmc.posofmyheart.infrastructure.persistence.entities;

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
    private String flavor;
    @ColumnDefault("true")
    private boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    public ProductFlavorEntity(Long idFlavor, String flavor, ProductEntity product) {
        this.idFlavor = idFlavor;
        this.flavor = flavor;
        this.product = product;
    }
}