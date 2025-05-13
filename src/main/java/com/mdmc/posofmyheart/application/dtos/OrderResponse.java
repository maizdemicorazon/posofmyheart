package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long orderId,
        LocalDateTime orderDate,
        BigDecimal totalAmount,
        String paymentMethod,
        String notes,
        List<OrderItemResponse> items
) {
    public record OrderItemResponse(
            Integer productId,
            String productName,
            BigDecimal bill
    ) {
    }
}