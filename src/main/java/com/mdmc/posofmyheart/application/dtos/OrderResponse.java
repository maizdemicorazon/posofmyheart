package com.mdmc.posofmyheart.application.dtos;

import com.mdmc.posofmyheart.domain.OrderStatusEnum;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponse(
        Long idOrder,
        LocalDateTime createdAt,
        BigDecimal bill,
        Long idPaymentMethod,
        String paymentName,
        String clientName,
        OrderStatusEnum status,
        Set<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Long idProduct,
            String productName,
            Long productImage,
            BigDecimal productPrice,
            Long idVariant,
            String variantName,
            String comment,
            Long idOrderDetail,
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