package com.mdmc.posofmyheart.application.services.impl;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.application.services.CatalogImageService;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;
import com.mdmc.posofmyheart.util.ImageUtils;

@Service
@RequiredArgsConstructor
@Log4j2
public class CatalogImageServiceImpl implements CatalogImageService {

    @Override
    public CatalogImageEntity processImageFromResource(
            String resourcePath,
            CatalogImageEntity.ImageType imageType,
            String fileName,
            String altText
    ) {

        try {
            log.debug("Procesando imagen desde recurso: {}", resourcePath);

            // Cargar imagen desde recursos
            ClassPathResource resource = new ClassPathResource(resourcePath);

            byte[] resizedImageData;

            try (InputStream inputStream = resource.getInputStream()) {
                byte[] imageData = inputStream.readAllBytes();
                resizedImageData = ImageUtils.resizeImageBytes(
                        imageData,
                        ImageUtils.MAX_WIDTH,
                        ImageUtils.MAX_HEIGHT,
                        true
                );
            }

            // Detectar tipo de contenido
            String contentType = ImageUtils.detectContentType(resizedImageData);

            // Procesar imagen
            return processImageFromBytes(resizedImageData, imageType, fileName, altText, contentType);

        } catch (Exception e) {
            log.error("Error procesando imagen desde recurso {}: {}", resourcePath, e.getMessage());
            throw new RuntimeException("Error procesando imagen desde recurso " + resourcePath, e);
        }
    }

    @Override
    public CatalogImageEntity processImageFromBytes(
            byte[] imageData,
            CatalogImageEntity.ImageType imageType,
            String fileName,
            String altText,
            String contentType) {

        try {
            // Validar datos de imagen
            validateImageData(imageData, contentType);

            // Obtener dimensiones de la imagen
            BufferedImage bufferedImage = ImageIO.read(new ByteArrayInputStream(imageData));
            if (bufferedImage == null) {
                throw new IllegalArgumentException("No se pudo leer la imagen como BufferedImage");
            }

            int width = bufferedImage.getWidth();
            int height = bufferedImage.getHeight();

            // Calcular checksum
            String checksum = calculateChecksum(imageData);

            // Construir entidad
            return CatalogImageEntity.builder()
                    .fileName(fileName)
                    .imageType(imageType)
                    .contentType(contentType)
                    .fileSize((long) imageData.length)
                    .width(width)
                    .height(height)
                    .imageData(imageData)
                    .checksum(checksum)
                    .altText(altText)
                    .active(true)
                    .build();

        } catch (Exception e) {
            log.error("Error procesando imagen {}: {}", fileName, e.getMessage());
            throw new RuntimeException("Error procesando imagen: " + fileName, e);
        }
    }

    @Override
    public String calculateChecksum(byte[] imageData) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(imageData);
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }

            return hexString.toString();

        } catch (Exception e) {
            log.error("Error calculando checksum: {}", e.getMessage());
            throw new RuntimeException("Error calculando checksum", e);
        }
    }

    @Override
    public void validateImageData(byte[] imageData, String contentType) {
        if (imageData == null || imageData.length == 0) {
            throw new IllegalArgumentException("Los datos de imagen no pueden estar vacíos");
        }

        if (imageData.length > 5 * 1024 * 1024) { // 5MB máximo
            throw new IllegalArgumentException("La imagen es demasiado grande (máximo 5MB)");
        }

        if (contentType == null || contentType.isBlank()) {
            throw new IllegalArgumentException("El tipo de contenido es requerido");
        }

        if (!contentType.startsWith("image/")) {
            throw new IllegalArgumentException("El tipo de contenido debe ser una imagen");
        }

        try {
            // Validar que sea una imagen válida
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageData));
            if (image == null) {
                throw new IllegalArgumentException("Los datos no representan una imagen válida");
            }

            // Validar dimensiones mínimas
            if (image.getWidth() < 1 || image.getHeight() < 1) {
                throw new IllegalArgumentException("Las dimensiones de la imagen no son válidas");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException("Error validando imagen: " + e.getMessage(), e);
        }
    }
}