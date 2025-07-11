package com.mdmc.posofmyheart.application.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import com.mdmc.posofmyheart.domain.models.ProductVariant;

public interface ProductVariantService {
    List<ProductVariant> getAllVariants();

    ProductVariant getVariantById(Long id);

    // Nuevos métodos para relación con productos
    List<ProductVariant> getVariantsByProductId(Long productId);

    List<ProductVariant> getActiveVariantsByProductId(Long productId);

    boolean isVariantAvailableForProduct(Long productId, Long variantId);

    List<ProductVariant> getVariantsByProductIdAndPriceRange(Long productId, BigDecimal minPrice, BigDecimal maxPrice);

    Optional<ProductVariant> getCheapestVariantByProductId(Long productId);

    Optional<ProductVariant> getMostExpensiveVariantByProductId(Long productId);

    Optional<ProductVariant> getVariantByProductIdAndSize(Long productId, String size);

    List<String> getAvailableSizesByProductId(Long productId);
}