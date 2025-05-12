package com.mdmc.posofmyheart.domain.models;

import java.util.List;

public record Product(
        Integer idProduct,
        Integer IdCategory,
        String name,
        String image,
        List<ProductVariant> options) {

    public Product {
        if (idProduct == null || idProduct <= 0) {
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

}
