package com.mdmc.posofmyheart.domain.patterns.builder;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.api.exceptions.ProductNotFoundException;
import com.mdmc.posofmyheart.api.exceptions.VariantNotFoundException;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
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
                .comment(request.comment())
                .orderDate(Optional.ofNullable(request.orderDate()).orElse(LocalDateTime.now()))
                .updatedAt(LocalDateTime.now())
                .build();
    }

    public OrderDetailEntity buildDetail(
            OrderItemRequest item,
            Map<Long, ProductEntity> products,
            Map<Long, ProductVariantEntity> variants
    ) {
        ProductEntity product = Optional.ofNullable(products.get(item.idProduct()))
                .orElseThrow(() -> new ProductNotFoundException(item.idProduct()));
        ProductVariantEntity variant = Optional.ofNullable(variants.get(item.idVariant()))
                .orElseThrow(() -> new VariantNotFoundException(item.idVariant()));

        return OrderDetailEntity.builder()
                .product(product)
                .variant(variant)
                .sellPrice(variant.getActualSellPrice())
                .productionCost(variant.getActualCostPrice())
                .sauceDetails(new HashSet<>())
                .extraDetails(new HashSet<>())
                .flavorDetails(new HashSet<>())
                .build();
    }
}
