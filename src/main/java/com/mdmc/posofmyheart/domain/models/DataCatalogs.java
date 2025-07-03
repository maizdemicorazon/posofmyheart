package com.mdmc.posofmyheart.domain.models;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.*;

import java.util.List;

public record DataCatalogs(
        List<PaymentMethodEntity> paymentMethods,
        List<ProductCategoryEntity> productCategories,
        List<ProductSauceEntity> productSauces,
        List<ProductExtraEntity> productExtras,
        List<ProductEntity> products,
        List<ProductFlavorEntity> productFlavors,
        List<ProductVariantEntity> productVariants
) {
}