package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

public record CatalogImageResponse(
        Long idImage,
        String fileName,
        String imageType,
        String contentType,
        Long fileSize,
        Integer width,
        Integer height,
        String altText,
        boolean active,
        java.time.LocalDateTime createdAt,
        boolean hasImageData
) {
    public static CatalogImageResponseBuilder builder() {
        return new CatalogImageResponseBuilder();
    }

    public static class CatalogImageResponseBuilder {
        private Long idImage;
        private String fileName;
        private String imageType;
        private String contentType;
        private Long fileSize;
        private Integer width;
        private Integer height;
        private String altText;
        private boolean active;
        private java.time.LocalDateTime createdAt;
        private boolean hasImageData;

        public CatalogImageResponseBuilder idImage(Long idImage) {
            this.idImage = idImage;
            return this;
        }

        public CatalogImageResponseBuilder fileName(String fileName) {
            this.fileName = fileName;
            return this;
        }

        public CatalogImageResponseBuilder imageType(String imageType) {
            this.imageType = imageType;
            return this;
        }

        public CatalogImageResponseBuilder contentType(String contentType) {
            this.contentType = contentType;
            return this;
        }

        public CatalogImageResponseBuilder fileSize(Long fileSize) {
            this.fileSize = fileSize;
            return this;
        }

        public CatalogImageResponseBuilder width(Integer width) {
            this.width = width;
            return this;
        }

        public CatalogImageResponseBuilder height(Integer height) {
            this.height = height;
            return this;
        }

        public CatalogImageResponseBuilder altText(String altText) {
            this.altText = altText;
            return this;
        }

        public CatalogImageResponseBuilder active(boolean active) {
            this.active = active;
            return this;
        }

        public CatalogImageResponseBuilder createdAt(java.time.LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public CatalogImageResponseBuilder hasImageData(boolean hasImageData) {
            this.hasImageData = hasImageData;
            return this;
        }

        public CatalogImageResponse build() {
            return new CatalogImageResponse(idImage, fileName, imageType,
                    contentType, fileSize, width, height, altText, active,
                    createdAt, hasImageData);
        }
    }

    public static CatalogImageResponse mapToResponse(CatalogImageEntity image) {
        return CatalogImageResponse.builder()
                .idImage(image.getIdImage())
                .fileName(image.getFileName())
                .contentType(image.getContentType())
                .fileSize(image.getFileSize())
                .width(image.getWidth())
                .height(image.getHeight())
                .altText(image.getAltText())
                .active(image.isActive())
                .createdAt(image.getCreatedAt())
                .hasImageData(image.getImageData() != null && image.getImageData().length > 0)
                .build();
    }
}