package com.mdmc.posofmyheart.domain.patterns.validator;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import org.springframework.stereotype.Component;

@Component
public class FlavorValidator {
    public void validateFlavorForProduct(ProductFlavorEntity flavor, ProductEntity product) {
        if (!flavor.getProduct().getIdProduct().equals(product.getIdProduct())) {
            throw new IllegalArgumentException("El sabor no corresponde al producto especificado");
        }

        if (!flavor.isActive()) {
            throw new IllegalArgumentException("El sabor seleccionado no está disponible");
        }
    }
}