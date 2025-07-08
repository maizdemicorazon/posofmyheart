package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.dtos.ProductImageInfo;
import com.mdmc.posofmyheart.application.services.ProductImageService;
import com.mdmc.posofmyheart.domain.dtos.ProductImageUploadResponse;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static com.mdmc.posofmyheart.util.ImageUtils.*;

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class ProductImageServiceImpl implements ProductImageService {

    private final ProductRepository productRepository;

    /**
     * Sube una imagen a un producto específico
     */
    @Override
    public ProductImageUploadResponse uploadImageToProduct(Long productId, MultipartFile file) {
        log.info("📤 Procesando carga de imagen para producto: {}", productId);

        validateFile(file);

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));

        try {
            // Procesar y optimizar imagen
            byte[] optimizedImage = processAndOptimizeImage(file);

            // Asignar imagen al producto
            product.setImage(optimizedImage);
            productRepository.save(product);

            log.info("✅ Imagen procesada y guardada para producto: {} (Tamaño: {} bytes)",
                    productId, optimizedImage.length);

            return
                    new ProductImageUploadResponse(
                    true,
                    "Imagen subida exitosamente",
                    productId,
                    file.getOriginalFilename(),
                    file.getContentType(),
                    optimizedImage.length
            );

        } catch (IOException e) {
            log.error("❌ Error procesando imagen: {}", e.getMessage(), e);
            throw new RuntimeException("Error procesando la imagen: " + e.getMessage());
        }
    }

    /**
     * Actualiza la imagen de un producto existente
     */
    @Override
    public ProductImageUploadResponse updateProductImage(Long productId, MultipartFile file) {
        log.info("🔄 Actualizando imagen para producto: {}", productId);

        return uploadImageToProduct(productId, file);
    }

    /**
     * Elimina la imagen de un producto
     */
    @Override
    public void deleteProductImage(Long productId) {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado con ID: " + productId));

        product.setImage(null);
        productRepository.save(product);

        log.info("🗑️ Imagen eliminada para producto: {}", productId);
    }

    /**
     * Obtiene información sobre la imagen de un producto
     */
    @Transactional(readOnly = true)
    @Override
    public ProductImageInfo getImageInfo(Long productId) throws IOException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado"));

        if (product.getImage() == null || product.getImage().length == 0) {
            return null;
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(product.getImage());
        BufferedImage image = ImageIO.read(bis);

        if (image == null) {
            throw new IOException("No se pudo leer la imagen almacenada");
        }

        return new ProductImageInfo(
                product.getImage().length,
                image.getWidth(),
                image.getHeight(),
                detectContentType(product.getImage())
        );
    }

    @Override
    public byte[] getImageById(Long idProduct) {
        log.debug("🔍 Obteniendo imagen de producto por id: {}", idProduct);

        long startTime = System.currentTimeMillis();
        byte[] productImage =   productRepository.getImageById(idProduct)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Imagen del producto con id {} no encontrada" + idProduct)
                );
        long endTime = System.currentTimeMillis();
        log.info("✅ Imagen del id {} obtenida en {}ms", idProduct, (endTime - startTime));
        return productImage;
    }

}
