package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long idOrder,
        LocalDateTime orderDate,
        BigDecimal bill,
        Long paymentMethod,
        String clientName,
        String comment,
        List<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Long idProduct,
            Long idVariant,
            List<OrderExtrasResponse> extras,
            List<OrderDetailSauceResponse> sauces,
            OrderFlavorDetailResponse flavor
    ) {
    }

    public record OrderExtrasResponse(
            Long idExtra,
            Integer quantity
    ) {
    }

    public record OrderDetailSauceResponse(
            Long idSauce,
            String name
    ) {
    }

    public record OrderFlavorDetailResponse(
            Long idFlavor,
            Long idFlavorDetail,
            String name,
            Long idOrderDetail
    ) {
    }
}