package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(
        @NotNull
        @Positive
        Long idPaymentMethod,
        @Size(max = 100)
        String comment,
        @NotEmpty
        List<OrderItemRequest> items,
        LocalDateTime orderDate
) {
}

