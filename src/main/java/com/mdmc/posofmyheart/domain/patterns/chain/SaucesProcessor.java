package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SaucesProcessor extends OrderItemProcessor {
    private final EntityFinder entityFinder;

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item) {
        if (detail.getSauceDetails() == null) {
            detail.setSauceDetails(new HashSet<>());
        }

        Optional.ofNullable(item.sauces())
                .orElseGet(Collections::emptyList)
                .forEach(sauce -> {
                    ProductSauceEntity productSauceEntity = entityFinder.findSauce(sauce.idSauce());
                    detail.addSauce(productSauceEntity);
                });

        processNext(detail, item);
    }

}
