package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

public interface CatalogImageService {

    CatalogImageEntity processImageFromResource(
            String resourcePath,
            CatalogImageEntity.ImageType imageType,
            String fileName,
            String altText
    );

    CatalogImageEntity processImageFromBytes(
            byte[] imageData,
            CatalogImageEntity.ImageType imageType,
            String fileName,
            String altText,
            String contentType);

    /**
     * Calcula checksum para imagen
     */
    String calculateChecksum(byte[] imageData);

    /**
     * Valida que la imagen sea válida
     */
    void validateImageData(byte[] imageData, String contentType);
}