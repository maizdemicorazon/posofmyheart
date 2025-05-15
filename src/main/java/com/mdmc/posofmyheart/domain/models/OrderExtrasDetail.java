package com.mdmc.posofmyheart.domain.models;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record OrderExtrasDetail(
        @NotNull(message = "El ID de extras es obligatorio")
        @Positive
        Long idExtra,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser al menos 1")
        @Positive
        Integer quantity
) {

    public OrderExtrasDetail {
        if (idExtra == null || idExtra <= 0) {
            throw new IllegalArgumentException("idExtra de producto inválido");
        }
        if (quantity == null || quantity <= 0) {
            throw new IllegalArgumentException("Precio de producto inválido");
        }
    }
}
