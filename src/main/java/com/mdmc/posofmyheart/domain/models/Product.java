package com.mdmc.posofmyheart.domain.models;

public record Product(
        Integer idProduct,
        Category category,
        String name,
        String description,
        String size,
        java.math.BigDecimal price) {

    public Product {
        if (idProduct == null || idProduct <= 0) {
            throw new IllegalArgumentException("ID de producto inválido");
        }
    }

}
