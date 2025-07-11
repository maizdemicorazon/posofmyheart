package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.List;

public record OrderRequest(
        @NotNull
        @Positive
        Long idPaymentMethod,
        @Size(max = 40)
        String clientName,
        LocalDateTime createdAt,
        @NotEmpty
        List<OrderItemRequest> items
) {
}

