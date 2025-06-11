package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
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

        @Valid
        List<OrderExtrasDetail> extras,

        @Valid
        List<ProductSauce> sauces,

        @Valid
        Long flavor
) {}
