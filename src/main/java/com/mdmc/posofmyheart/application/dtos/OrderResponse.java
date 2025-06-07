package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Integer idOrder,
        LocalDateTime orderDate,
        BigDecimal bill,
        Integer paymentMethod,
        String clientName,
        String comment,
        List<OrderItemResponse> items
) {

    public record OrderItemResponse(
            Integer idProduct,
            Integer idVariant,
            List<OrderExtrasResponse> extras,
            List<OrderDetailSauceResponse> sauces,
            OrderFlavorDetailResponse flavor
    ) {

        public OrderItemResponse(
                Integer idProduct,
                Integer idVariant,
                List<OrderExtrasResponse> extras,
                List<OrderDetailSauceResponse> sauces
        ) {
            this(idProduct, idVariant, extras, sauces, null);
        }
    }

    public record OrderExtrasResponse(
            Integer idExtra,
            Integer quantity
    ) {
    }

    public record OrderDetailSauceResponse(
            Integer idSauce,
            String name
    ) {
    }

    public record OrderFlavorDetailResponse(
            Integer idFlavorDetail,
            String name,
            Long idOrderDetail
    ) {
    }
}