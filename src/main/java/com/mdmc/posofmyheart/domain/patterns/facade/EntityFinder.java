package com.mdmc.posofmyheart.domain.patterns.facade;

import com.mdmc.posofmyheart.api.exceptions.*;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

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