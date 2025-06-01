package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductExtra(
        Long idExtra,
        String name,
        BigDecimal price
) {
    public ProductExtra {
        if (idExtra == null || idExtra <= 0) {
            throw new IllegalArgumentException("idExtra de producto inválido");
        }
        if (price == null) {
            throw new IllegalArgumentException("Precio de producto inválido");
        }
        if (name == null || name.isBlank()){
            throw new IllegalArgumentException("Nombre de extra inválido");
        }
    }
}
