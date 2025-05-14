package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductExtra(
        Integer idExtra,
        String name,
        BigDecimal price
) {
    public ProductExtra {
        if (idExtra == null || idExtra <= 0) {
            throw new IllegalArgumentException("idExtra de producto inválido");
        }
        if (price == null || price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Precio de producto inválido");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nombre de extra inválido");
        }
    }
}
