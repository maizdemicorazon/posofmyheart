package com.mdmc.posofmyheart.domain.dtos;

import jakarta.validation.constraints.NotNull;

public record CreateOrderResponse(
        @NotNull
        Long idOrder
) {}