package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderItemRequest(
        @NotNull(message = "Product ID is required")
        @Positive(message = "Product ID must be positive")
        Long idProduct,

        @NotNull(message = "Variant ID is required")
        @Positive(message = "Variant ID must be positive")
        Long idVariant,

        @Size(max = 100)
        @NotEmpty
        String comment,

        @Valid
        List<OrderExtrasDetail> extras,

        @Valid
        List<ProductSauce> sauces,

        @Valid
        Long flavor
) {}
