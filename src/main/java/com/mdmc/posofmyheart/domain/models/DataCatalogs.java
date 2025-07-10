package com.mdmc.posofmyheart.domain.models;

import java.util.List;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductCategoryEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

public record DataCatalogs(
        List<PaymentMethodEntity> paymentMethods,
        List<ProductCategoryEntity> productCategories,
        List<ProductSauceEntity> productSauces,
        List<ProductExtraEntity> productExtras,
        List<ProductEntity> products,
        List<ProductFlavorEntity> productFlavors,
        List<ProductVariantEntity> productVariants,
        List<CatalogImageEntity> catalogImages
) {
}