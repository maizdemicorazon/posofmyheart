package com.mdmc.posofmyheart.domain.patterns.builder;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductVariantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OrderBuilder {
    private final EntityFinder entityFinder;

    public OrderEntity buildFromRequest(OrderRequest request) {
        PaymentMethodEntity paymentMethod = entityFinder.findPaymentMethod(request.idPaymentMethod());

        return OrderEntity.builder()
                .paymentMethod(paymentMethod)
                .clientName(request.clientName())
                .orderDate(Optional.ofNullable(request.orderDate()).orElse(LocalDateTime.now()))
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
                .comment(item.comment())
                .sauceDetails(new HashSet<>())
                .extraDetails(new HashSet<>())
                .flavorDetails(new HashSet<>())
                .build();
    }
}
