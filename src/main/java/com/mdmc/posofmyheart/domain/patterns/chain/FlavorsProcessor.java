package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.domain.patterns.validator.FlavorValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderFlavorDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

@Component
public class FlavorsProcessor extends OrderItemProcessor {
    private final EntityFinder entityFinder;
    private final FlavorValidator flavorValidator;

    public FlavorsProcessor(EntityFinder entityFinder, FlavorValidator flavorValidator) {
        this.entityFinder = entityFinder;
        this.flavorValidator = flavorValidator;
    }

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item) {
        Optional.ofNullable(item.flavor())
                .ifPresent(flavor -> addFlavorToDetail(detail, flavor));

        processNext(detail, item);
    }

    private void addFlavorToDetail(OrderDetailEntity detail, Long idFlavor) {
        ProductFlavorEntity flavorEntity = entityFinder.findFlavor(idFlavor);
        flavorValidator.validateFlavorForProduct(flavorEntity, detail.getProduct());

        OrderFlavorDetailEntity flavorDetail = new OrderFlavorDetailEntity(detail, flavorEntity);
        flavorDetail.setCreatedAt(LocalDateTime.now());
        detail.getFlavorDetails().add(flavorDetail);
    }
}