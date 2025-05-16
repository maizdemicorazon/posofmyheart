package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotNull @Positive Long idPaymentMethod,
        @Size(max = 100) String comment,
        @NotEmpty List<OrderItemRequest> items
) {
}

