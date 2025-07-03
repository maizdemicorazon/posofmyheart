package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.OrderStatusEnum;
import jakarta.validation.constraints.NotNull;

public record OrderStatusRequest(
        @NotNull
        OrderStatusEnum status
) {
}

