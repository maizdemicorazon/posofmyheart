package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder(setterPrefix = "with")
public record OrderRequest(
        @NotNull
        @Positive
        Long idPaymentMethod,
        @Size(max = 40)
        String clientName,
        @Size(max = 100)
        String comment,
        @NotEmpty
        List<OrderItemRequest> items,
        LocalDateTime orderDate
) {
}

