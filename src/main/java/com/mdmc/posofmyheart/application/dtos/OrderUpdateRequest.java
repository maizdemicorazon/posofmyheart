package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderUpdateRequest(
        @Size(max = 100)
        String comment,
        @Positive
        Long idPaymentMethod,
        @Valid
        List<OrderItemUpdate> updatedItems) {

    public record OrderItemUpdate(
            @Positive
            Long idOrderDetail,// Null para nuevos items
            @Positive
            Long idProduct,
            @Positive
            Long idVariant,
            @Positive
            Long idSauce,
            @Valid
            List<ProductExtraUpdate> updatedExtras,
            List<SauceUpdate> updatedSauces
    ) {
    }

    public record ProductExtraUpdate(
            @Positive
            Long idExtra,
            @Min(1)
            Integer quantity
    ) {
    }

    public record SauceUpdate(Long idSauce) {}

}

