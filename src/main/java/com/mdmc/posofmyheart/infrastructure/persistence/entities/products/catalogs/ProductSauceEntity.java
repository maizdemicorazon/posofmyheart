package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
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
@Table(name = "product_sauces", indexes = {
        @Index(name = "idx_product_sauce_name", columnList = "name"),
        @Index(name = "idx_product_sauce_image", columnList = "id_image")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductSauceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sauce")
    private Long idSauce;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_image")
    private CatalogImageEntity image;

    @Column(nullable = false)
    private boolean active = true;

    public byte[] getImageData() {
        return image != null && image.isActive() ? image.getImageDataSafe() : new byte[0];
    }

    public boolean hasImage() {
        return image != null && image.isActive();
    }

    public ProductSauceEntity(Long idSauce, String name, String description, CatalogImageEntity image) {
        this.idSauce = idSauce;
        this.name = name;
        this.description = description;
        this.image = image;
    }
}