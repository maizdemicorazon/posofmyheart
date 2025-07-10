package com.mdmc.posofmyheart.domain.models;

public record ProductFlavor(
        Long idFlavor,
        String name,
        byte[] image
) {
    public ProductFlavor {
        if (idFlavor == null || idFlavor <= 0) {
            throw new IllegalArgumentException("idFlavor es inválido");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nombre de sabor inválido");
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