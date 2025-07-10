package com.mdmc.posofmyheart.domain.models;

public record ProductSauce(
        Long idSauce,
        String name,
        byte[] image
) {

    public ProductSauce {
        if (idSauce == null || idSauce <= 0) {
            throw new IllegalArgumentException("idSauce es una elección inválida");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Nombre de salsa inválido");
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