package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;

public record OrderResponse(
        Long idOrder,
        BigDecimal bill
) {
}