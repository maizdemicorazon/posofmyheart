package com.mdmc.posofmyheart.domain.patterns.builder;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderBuilder {
    private final EntityFinder entityFinder;

    public OrderEntity buildFromRequest(OrderRequest request) {
        PaymentMethodEntity paymentMethod = entityFinder.findPaymentMethod(request.idPaymentMethod());

        LocalDateTime orderDate = Optional.ofNullable(request.orderDate()).orElse(LocalDateTime.now());
        return OrderEntity.builder()
                .paymentMethod(paymentMethod)
                .clientName(request.clientName())
                .comment(request.comment())
                .orderDate(orderDate)
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public OrderDetailEntity buildDetail(OrderItemRequest item) {
        ProductEntity product = entityFinder.findProduct(item.idProduct());
        ProductVariantEntity variant = entityFinder.findVariant(item.idVariant());

        return OrderDetailEntity.builder()
                .product(product)
                .variant(variant)
                .sellPrice(variant.getActualSellPrice())
                .productionCost(variant.getActualCostPrice())
                .sauceDetails(new ArrayList<>())
                .extraDetails(new ArrayList<>())
                .flavorDetails(new ArrayList<>())
                .build();
    }
}
