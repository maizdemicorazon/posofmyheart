package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponseCreate(
        Integer idOrder,
        LocalDateTime orderDate,
        Integer idPaymentMethod,
        String clientName,
        String comment,
        List<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Integer idProduct,
            Integer idVariant,
            List<OrderExtrasResponse> extras,
            List<OrderDetailSauceResponse> sauces,
            Integer flavor
    ) {
    }

    public record OrderExtrasResponse(
            Integer idExtra,
            Integer quantity
    ) {
    }

    public record OrderDetailSauceResponse(
            Integer idSauce
    ) {
    }

}