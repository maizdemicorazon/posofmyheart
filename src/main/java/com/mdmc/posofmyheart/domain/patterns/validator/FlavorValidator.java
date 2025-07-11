package com.mdmc.posofmyheart.domain.patterns.validator;

import org.springframework.stereotype.Component;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;

@Component
public class FlavorValidator {
    public void validateFlavorForProduct(ProductFlavorEntity flavor, ProductEntity product) {
        if (!flavor.getProduct().getIdProduct().equals(product.getIdProduct())) {
            throw new IllegalArgumentException("El sabor no corresponde al producto especificado ["
                    .concat("flavor: ")
                    .concat(flavor.getIdFlavor().toString())
                    .concat(" Product: ")
                    .concat(product.getIdProduct().toString()
                            .concat("]")));

        }

        if (!flavor.isActive()) {
            throw new IllegalArgumentException("El sabor seleccionado no está disponible");
        }
    }
}