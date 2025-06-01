package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderUpdateRequest(
        @Positive
        Long idPaymentMethod,
        @Size(max = 100)
        String comment,
        List<OrderItemUpdate> updatedItems
) {
    public record OrderItemUpdate(
            Long idOrderDetail, // null para nuevos items
            @Positive
            Long idProduct,
            @Positive
            Long idVariant,
            List<ProductExtraUpdate> updatedExtras,
            List<SauceUpdate> updatedSauces,
            List<FlavorUpdate> updatedFlavors // Nueva lista para sabores
    ) {}

    public record ProductExtraUpdate(
            @Positive
            Long idExtra,
            @Positive
            Integer quantity
    ) {}

    public record SauceUpdate(
            @Positive
            Long idSauce
    ) {}

    public record FlavorUpdate(
            @Positive
            Long idFlavor
    ) {}
}
