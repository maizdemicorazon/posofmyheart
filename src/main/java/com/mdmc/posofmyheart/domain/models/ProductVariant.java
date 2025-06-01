package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductVariant(
        Long idVariant,
        String size,
        BigDecimal price
) {
    public ProductVariant {
        if (size == null || size.isBlank()) {
            throw new IllegalArgumentException("Tamaño inválido");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio de tamaño inválido");
        }
    }
}
