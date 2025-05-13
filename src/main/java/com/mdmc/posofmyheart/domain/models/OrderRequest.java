package com.mdmc.posofmyheart.domain.models;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotNull Long paymentMethodId,
        @Size(max = 100) String notes,
        @NotEmpty List<OrderItemRequest> items
) {
}

