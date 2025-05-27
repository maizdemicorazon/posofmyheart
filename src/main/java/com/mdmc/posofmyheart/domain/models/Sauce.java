package com.mdmc.posofmyheart.domain.models;

public record Sauce(
        Long idSauce,
        String name
) {

    public Sauce{
        if (idSauce == null || idSauce <= 0) {
            throw new IllegalArgumentException("idSauce es una elección inválida");
        }
    }

}
