package com.mdmc.posofmyheart.domain.models;

public record ProductFlavor(
        Long idFlavor,
        String name,
        Long idImage
) {
    public ProductFlavor {
        if (idFlavor == null || idFlavor <= 0) {
            throw new IllegalArgumentException("idFlavor es inválido");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nombre de sabor inválido");
        }

    }
}
