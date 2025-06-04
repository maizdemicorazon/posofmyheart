package com.mdmc.posofmyheart.domain.patterns.facade;

import com.mdmc.posofmyheart.api.exceptions.*;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
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
                .orElseThrow(PayMethodNotFoundException::new);
    }

    public ProductEntity findProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public ProductVariantEntity findVariant(Long id) {
        return variantRepository.findById(id)
                .orElseThrow(VariantNotFoundException::new);
    }

    public ProductExtraEntity findProductExtra(Long id) {
        return productExtraRepository.findById(id)
                .orElseThrow(ProductExtraNotFoundException::new);
    }

    public ProductSauceEntity findSauce(Long id) {
        return productSauceRepository.findById(id)
                .orElseThrow(SauceNotFoundException::new);
    }

    public ProductFlavorEntity findFlavor(Long id) {
        return flavorRepository.findById(id)
                .orElseThrow(FlavorNotFoundException::new);
    }
}