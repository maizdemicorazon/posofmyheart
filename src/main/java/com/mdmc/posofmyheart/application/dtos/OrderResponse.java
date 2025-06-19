package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponse(
        Long idOrder,
        LocalDateTime orderDate,
        BigDecimal bill,
        Long paymentMethod,
        String paymentName,
        String clientName,
        String comment,
        Set<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Long idProduct,
            String productName,
            String productImage,
            BigDecimal productPrice,
            Long idVariant,
            String variantName,
            Set<OrderExtrasResponse> extras,
            Set<OrderDetailSauceResponse> sauces,
            OrderFlavorDetailResponse flavor
    ) {
    }

    public record OrderExtrasResponse(
            String name,
            Long idExtra,
            Integer quantity,
            BigDecimal actualPrice
    ) {
    }

    public record OrderDetailSauceResponse(
            Long idSauce,
            String name,
            String image
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