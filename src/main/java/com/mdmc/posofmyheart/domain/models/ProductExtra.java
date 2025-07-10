package com.mdmc.posofmyheart.domain.models;

import java.math.BigDecimal;

public record ProductExtra(
        Long idExtra,
        String name,
        BigDecimal price,
        byte[] image
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
        // Inicializar imagen vacía si es null
        if (image == null) {
            image = new byte[0];
        }
    }

    // Método de conveniencia para verificar si tiene imagen
    public boolean hasImage() {
        return image != null && image.length > 0;
    }
}