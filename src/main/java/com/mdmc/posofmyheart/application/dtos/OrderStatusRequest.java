package com.mdmc.posofmyheart.application.dtos;

import jakarta.validation.constraints.NotNull;

import com.mdmc.posofmyheart.domain.OrderStatusEnum;

public record OrderStatusRequest(
        @NotNull
        OrderStatusEnum status
) {
}

