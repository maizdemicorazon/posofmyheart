package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer idOrder,
        LocalDateTime orderDate,
        BigDecimal bill,
        Integer paymentMethod,
        String comment,
        List<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Integer idProduct,
            Integer idSauce,
            Integer idVariant,
            List<OrderExtrasResponse> extras
    ) {
    }

    public record OrderExtrasResponse(
            Integer idExtra,
            Integer quantity
    ) {
    }
}