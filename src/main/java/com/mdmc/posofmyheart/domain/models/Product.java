package com.mdmc.posofmyheart.domain.models;

import java.util.List;

public record Product(
        Integer idProduct,
        Category category,
        String name,
        String image,
        String description,
        List<ProductVariant> options) {

    public Product {
        if (idProduct == null || idProduct <= 0) {
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

}
