package com.mdmc.posofmyheart.application.services.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.VariantNotFoundException;
import com.mdmc.posofmyheart.application.mappers.ProductVariantMapper;
import com.mdmc.posofmyheart.application.services.ProductVariantService;
import com.mdmc.posofmyheart.domain.models.ProductVariant;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductVariantRepository;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductVariantServiceImpl implements ProductVariantService {

    private final ProductVariantRepository variantRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional(readOnly = true)
    public List<ProductVariant> getAllVariants() {
        log.debug("Getting all variants");
        return ProductVariantMapper.INSTANCE.toDomainVariants(variantRepository.findAll());
    }

    @Override
    @Transactional(readOnly = true)
    public ProductVariant getVariantById(Long id) {
        log.debug("Getting variant by id: {}", id);
        return ProductVariantMapper.INSTANCE.toProductVariant(
                variantRepository.findById(id)
                        .orElseThrow(() -> new VariantNotFoundException(id))
        );
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'product_' + #productId")
    public List<ProductVariant> getVariantsByProductId(Long productId) {
        log.debug("Getting all variants for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return variantRepository.findVariantsByProductId(productId)
                .stream()
                .map(ProductVariantMapper.INSTANCE::toProductVariant)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'active_product_' + #productId")
    public List<ProductVariant> getActiveVariantsByProductId(Long productId) {
        log.debug("Getting active variants for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return variantRepository.findActiveVariantsByProductId(productId)
                .stream()
                .map(ProductVariantMapper.INSTANCE::toProductVariant)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isVariantAvailableForProduct(Long productId, Long variantId) {
        log.debug("Checking if variant {} is available for product {}", variantId, productId);

        // Verificar que ambos existen
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        variantRepository.findById(variantId)
                .orElseThrow(() -> new VariantNotFoundException(variantId));

        return variantRepository.existsByProductIdAndVariantId(productId, variantId);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'product_' + #productId + '_price_' + #minPrice + '_' + #maxPrice")
    public List<ProductVariant> getVariantsByProductIdAndPriceRange(Long productId, BigDecimal minPrice,
                                                                    BigDecimal maxPrice) {
        log.debug("Getting variants for product {} with price range: {} - {}", productId, minPrice, maxPrice);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        // Validar que el rango de precios es válido
        if (minPrice.compareTo(maxPrice) > 0) {
            throw new IllegalArgumentException("Min price cannot be greater than max price");
        }

        return variantRepository.findVariantsByProductIdAndPriceRange(productId, minPrice, maxPrice)
                .stream()
                .map(ProductVariantMapper.INSTANCE::toProductVariant)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'cheapest_product_' + #productId")
    public Optional<ProductVariant> getCheapestVariantByProductId(Long productId) {
        log.debug("Getting cheapest variant for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return variantRepository.findCheapestVariantByProductId(productId)
                .map(ProductVariantMapper.INSTANCE::toProductVariant);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'expensive_product_' + #productId")
    public Optional<ProductVariant> getMostExpensiveVariantByProductId(Long productId) {
        log.debug("Getting most expensive variant for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return variantRepository.findMostExpensiveVariantByProductId(productId)
                .map(ProductVariantMapper.INSTANCE::toProductVariant);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'product_' + #productId + '_size_' + #size")
    public Optional<ProductVariant> getVariantByProductIdAndSize(Long productId, String size) {
        log.debug("Getting variant for product {} with size: {}", productId, size);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (size == null || size.trim().isEmpty()) {
            throw new IllegalArgumentException("Size cannot be null or empty");
        }

        return variantRepository.findByProductIdAndSize(productId, size.trim())
                .map(ProductVariantMapper.INSTANCE::toProductVariant);
    }

    @Override
    @Transactional(readOnly = true)
    @Cacheable(value = "variants", key = "'sizes_product_' + #productId")
    public List<String> getAvailableSizesByProductId(Long productId) {
        log.debug("Getting available sizes for product id: {}", productId);

        // Verificar que el producto existe
        productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        return variantRepository.findDistinctSizesByProductId(productId);
    }
}