package com.mdmc.posofmyheart.domain.patterns.facade;

import com.mdmc.posofmyheart.api.exceptions.*;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

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

    public Map<Long, ProductEntity> findProducts(Collection<Long> ids) {
        Map<Long, ProductEntity> map = productRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(ProductEntity::getIdProduct, Function.identity()));
        ids.stream()
                .filter(id -> !map.containsKey(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new ProductNotFoundException(id);
                });
        return map;
    }

    public Map<Long, ProductVariantEntity> findVariants(Collection<Long> ids) {
        Map<Long, ProductVariantEntity> map = variantRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(ProductVariantEntity::getIdVariant, Function.identity()));
        ids.stream()
                .filter(id -> !map.containsKey(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new VariantNotFoundException(id);
                });
        return map;
    }

    public Map<Long, ProductExtraEntity> findProductExtras(Collection<Long> ids) {
        Map<Long, ProductExtraEntity> map = productExtraRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(ProductExtraEntity::getIdExtra, Function.identity()));
        ids.stream()
                .filter(id -> !map.containsKey(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new ProductExtraNotFoundException(id);
                });
        return map;
    }

    public Map<Long, ProductSauceEntity> findSauces(Collection<Long> ids) {
        Map<Long, ProductSauceEntity> map = productSauceRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(ProductSauceEntity::getIdSauce, Function.identity()));
        ids.stream()
                .filter(id -> !map.containsKey(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new ProductSauceNotFoundException(id);
                });
        return map;
    }

    public Map<Long, ProductFlavorEntity> findFlavors(Collection<Long> ids) {
        Map<Long, ProductFlavorEntity> map = flavorRepository.findAllById(ids)
                .stream()
                .collect(Collectors.toMap(ProductFlavorEntity::getIdFlavor, Function.identity()));
        ids.stream()
                .filter(id -> !map.containsKey(id))
                .findFirst()
                .ifPresent(id -> {
                    throw new FlavorNotFoundException(id);
                });
        return map;
    }
}