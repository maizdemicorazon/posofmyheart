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
    }

}
