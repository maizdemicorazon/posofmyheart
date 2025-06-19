package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

@Component
public class SaucesProcessor extends OrderItemProcessor {
    

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded) {
        if (detail.getSauceDetails() == null) {
            detail.setSauceDetails(new HashSet<>());
        }

        Optional.ofNullable(item.sauces())
                .orElseGet(Collections::emptyList)
                .forEach(sauce -> {
                    ProductSauceEntity productSauceEntity = Optional.ofNullable(preloaded.sauces().get(sauce.idSauce()))
                            .orElseThrow(() -> new com.mdmc.posofmyheart.api.exceptions.ProductSauceNotFoundException(sauce.idSauce()));
                    detail.addSauce(productSauceEntity);
                });

        processNext(detail, item, preloaded);
    }

}
