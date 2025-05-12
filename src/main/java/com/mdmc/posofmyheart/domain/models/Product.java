package com.mdmc.posofmyheart.domain.models;

import java.util.List;

public record Product(
        Integer id,
        Integer IdCategory,
        String name,
        String image,
        List<ProductVariant> options) {

    public Product {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

}
