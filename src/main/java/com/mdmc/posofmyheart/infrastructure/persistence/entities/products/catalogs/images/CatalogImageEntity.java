package com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.BaseEntity;

@Entity
@Table(name = "catalog_images", indexes = {
        @Index(name = "idx_catalog_images_type", columnList = "imageType"),
        @Index(name = "idx_catalog_images_active", columnList = "active"),
        @Index(name = "idx_catalog_images_checksum", columnList = "checksum"),
        @Index(name = "idx_catalog_images_created", columnList = "createdAt")
})

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CatalogImageEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_image")
    private Long idImage;

    @Column(name = "file_name", nullable = false)
    private String fileName;

    @Enumerated(EnumType.STRING)
    @Column(name = "image_type", nullable = false)
    private ImageType imageType;

    @Column(name = "content_type", nullable = false)
    private String contentType;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;

    @Lob
    @Column(name = "image_data", nullable = false)
    private byte[] imageData;

    @Column(name = "checksum", nullable = false, unique = true)
    private String checksum;

    @Column(name = "alt_text")
    private String altText;

    @Column(nullable = false)
    private boolean active;

    @Transient
    private String resource;

    public enum ImageType {
        PRODUCT_MAIN,
        PRODUCT_FLAVOR,
        PRODUCT_EXTRA,
        PRODUCT_SAUCE,
        PRODUCT_BASIC,
        UNKNOWN
    }
}
