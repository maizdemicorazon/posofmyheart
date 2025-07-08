package com.mdmc.posofmyheart.domain.models;

import lombok.Builder;

import java.util.List;

@Builder
public record Product(
        Long idProduct,
        Long idCategory,
        String name,
        byte[] image,
        List<ProductVariant> options,
        List<ProductFlavor> flavors) {

    public Product {
        if (idProduct == null || idProduct <= 0) {
            throw new IllegalArgumentException("idProduct inválido");
        }
        if (idCategory == null || idCategory <= 0) {
            throw new IllegalArgumentException("idCategory de producto inválido");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nombre de producto inválido");
        }

    }

}
