package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.models.Sauce;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.util.List;

public record OrderItemRequest(
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long idProduct,

        @NotNull(message = "Variant ID is required")
        @Positive(message = "Variant ID must be positive")
        Long idVariant,

        @NotNull(message = "Quantity is required")
        @Positive(message = "Quantity must be positive")
        Integer quantity,

        @Valid
        List<OrderExtrasDetail> extras,

        @Valid
        List<Sauce> sauces,

        @Valid
        Long flavor
) {}
