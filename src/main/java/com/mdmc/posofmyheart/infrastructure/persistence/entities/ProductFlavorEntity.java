package com.mdmc.posofmyheart.infrastructure.persistence.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "product_flavors", indexes = {
        @Index(name = "idx_flavors_id_product", columnList = "id_product")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductFlavorEntity {
    @EmbeddedId
    private ProductFlavorKey productFlavorKey;

    private String flavor;
    private boolean active = true;

    @MapsId("idProduct")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_product", nullable = false)
    private ProductEntity product;

    public ProductFlavorEntity(Long idFlavor, String flavor, ProductEntity product) {
        this.productFlavorKey = new ProductFlavorKey(idFlavor, product.getIdProduct());
        this.flavor = flavor;
        this.product = product;
    }
}