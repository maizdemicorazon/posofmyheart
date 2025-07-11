package com.mdmc.posofmyheart.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

/**
 * Mapper utilitario común para conversión de imágenes de catálogo
 * Evita duplicación de métodos de mapeo en diferentes mappers
 */
@Mapper
public interface CatalogImageMapper {

    CatalogImageMapper INSTANCE = Mappers.getMapper(CatalogImageMapper.class);

    /**
     * Convierte CatalogImageEntity a byte[] de forma segura
     * Método centralizado para evitar ambigüedad en MapStruct
     */
    @Named("catalogImageToByteArray")
    default byte[] catalogImageToByteArray(CatalogImageEntity catalogImage) {
        if (catalogImage == null || !catalogImage.isActive()) {
            return new byte[0];
        }
        return catalogImage.getImageDataSafe();
    }

    /**
     * Valida si una imagen está disponible
     */
    @Named("isImageAvailable")
    default boolean isImageAvailable(CatalogImageEntity catalogImage) {
        return catalogImage != null && catalogImage.isActive();
    }

    /**
     * Obtiene el texto alternativo de la imagen
     */
    @Named("getImageAltText")
    default String getImageAltText(CatalogImageEntity catalogImage) {
        return catalogImage != null ? catalogImage.getAltText() : "";
    }

    /**
     * Obtiene el tipo de contenido de la imagen
     */
    @Named("getImageContentType")
    default String getImageContentType(CatalogImageEntity catalogImage) {
        return catalogImage != null ? catalogImage.getContentType() : "image/jpeg";
    }
}