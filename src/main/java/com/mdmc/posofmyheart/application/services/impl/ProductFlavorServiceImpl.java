package com.mdmc.posofmyheart.application.services.impl;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.api.exceptions.FlavorNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductFlavorMapper;
import com.mdmc.posofmyheart.application.services.ProductFlavorService;
import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductFlavorRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductFlavorServiceImpl implements ProductFlavorService {

    private final ProductFlavorRepository flavorRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'flavor-' + #idFlavor")
    public ProductFlavor getFlavorById(Long idFlavor) {
        log.debug("🔍 Obteniendo sabor por ID: {}", idFlavor);

        return flavorRepository.findById(idFlavor)
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .orElseThrow(() -> new FlavorNotFoundException(idFlavor));
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'all-flavors'")
    public List<ProductFlavor> getAllFlavors() {
        log.debug("🔍 Obteniendo todos los sabores con imágenes");

        long startTime = System.currentTimeMillis();

        List<ProductFlavor> flavors = flavorRepository.findActiveWithImages()
                .stream()
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} sabores obtenidos con imágenes en {}ms", flavors.size(), (endTime - startTime));

        return flavors;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'product_' + #productId")
    public List<ProductFlavor> getFlavorsByProductId(Long productId) {
        log.debug("🔍 Obteniendo todos los sabores para producto ID: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        long startTime = System.currentTimeMillis();

        List<ProductFlavor> flavors = flavorRepository.findAllFlavorsByProductIdWithProduct(productId)
                .stream()
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} sabores obtenidos para producto {} con imágenes en {}ms",
                flavors.size(), productId, (endTime - startTime));

        return flavors;
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "flavors", key = "'active_product_' + #productId")
    public List<ProductFlavor> getActiveFlavorsByProductId(Long productId) {
        log.debug("🔍 Obteniendo sabores activos para producto ID: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        long startTime = System.currentTimeMillis();

        List<ProductFlavor> flavors = flavorRepository.findActiveFlavorsByProductIdWithProduct(productId)
                .stream()
                .map(ProductFlavorMapper.INSTANCE::toProductFlavor)
                .toList();

        long endTime = System.currentTimeMillis();
        log.info("✅ {} sabores activos obtenidos para producto {} con imágenes en {}ms",
                flavors.size(), productId, (endTime - startTime));

        return flavors;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isFlavorAvailableForProduct(Long productId, Long flavorId) {
        log.debug("🔍 Verificando disponibilidad de sabor {} para producto {}", flavorId, productId);

        // Verificar que ambos existen
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        flavorRepository.findById(flavorId)
                .orElseThrow(() -> new FlavorNotFoundException(flavorId));

        boolean isAvailable = flavorRepository.existsByProductIdAndFlavorId(productId, flavorId);

        log.debug("✅ Sabor {} {} disponible para producto {}",
                flavorId, isAvailable ? "está" : "no está", productId);

        return isAvailable;
    }
}