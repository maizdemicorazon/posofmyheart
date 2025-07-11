package com.mdmc.posofmyheart.domain.patterns.facade;

import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;

import com.mdmc.posofmyheart.api.exceptions.FlavorNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.PayMethodNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductExtraNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.ProductSauceNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.VariantNotFoundException;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductExtraRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductFlavorRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductSauceRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductVariantRepository;

@Component
@AllArgsConstructor
public class EntityFinder {
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final ProductVariantRepository variantRepository;
    private final ProductExtraRepository productExtraRepository;
    private final ProductSauceRepository productSauceRepository;
    private final ProductFlavorRepository flavorRepository;

    public PaymentMethodEntity findPaymentMethod(Long id) {
        return paymentMethodRepository.findById(id)
                .orElseThrow(() -> new PayMethodNotFoundException(id));
    }

    public ProductEntity findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public ProductVariantEntity findVariant(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(() -> new VariantNotFoundException(id));
    }

    public ProductExtraEntity findProductExtra(Long id) {
        return productExtraRepository.findById(id)
                .orElseThrow(() -> new ProductExtraNotFoundException(id));
    }

    public ProductSauceEntity findSauce(Long id) {
        return productSauceRepository.findById(id)
                .orElseThrow(() -> new ProductSauceNotFoundException(id));
    }

    public ProductFlavorEntity findFlavor(Long id) {
        return flavorRepository.findById(id)
                .orElseThrow(() -> new FlavorNotFoundException(id));
    }
}