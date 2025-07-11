package com.mdmc.posofmyheart.domain.patterns.builder;

import java.util.HashSet;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderBuilder {
    private final EntityFinder entityFinder;

    public OrderEntity buildFromRequest(OrderRequest request) {
        PaymentMethodEntity paymentMethod = entityFinder.findPaymentMethod(request.idPaymentMethod());

        return OrderEntity.builder()
                .paymentMethod(paymentMethod)
                .clientName(request.clientName())
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
