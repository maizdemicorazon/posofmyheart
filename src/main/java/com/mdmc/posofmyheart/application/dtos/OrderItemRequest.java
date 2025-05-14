package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.models.ProductExtrasDetail;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderItemRequest(
        @NotNull(message = "El ID de la orden es obligatorio")
        @Positive
        Integer idOrder,
        @Positive
        @NotNull(message = "El ID del producto es obligatorio")
        @Positive
        Integer idProduct,
        @NotNull(message = "El ID de la salsa es obligatorio")
        @Positive
        Integer idSauce,
        @NotNull(message = "El ID del tamaño es obligatorio")
        @Positive
        Integer idVariant,
        List<ProductExtrasDetail> extras
) {
}
