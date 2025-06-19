package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;
import com.mdmc.posofmyheart.domain.patterns.validator.FlavorValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderFlavorDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class FlavorsProcessor extends OrderItemProcessor {
    private final FlavorValidator flavorValidator;

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded) {
        Optional.ofNullable(item.flavor())
                .ifPresent(flavor -> addFlavorToDetail(detail, flavor, preloaded.flavors()));

        processNext(detail, item, preloaded);
    }

    private void addFlavorToDetail(OrderDetailEntity detail, Long idFlavor, Map<Long, ProductFlavorEntity> flavors) {
        ProductFlavorEntity flavorEntity = Optional.ofNullable(flavors.get(idFlavor))
                .orElseThrow(() -> new com.mdmc.posofmyheart.api.exceptions.FlavorNotFoundException(idFlavor));
        flavorValidator.validateFlavorForProduct(flavorEntity, detail.getProduct());

        OrderFlavorDetailEntity flavorDetail = new OrderFlavorDetailEntity(detail, flavorEntity);
        flavorDetail.setCreatedAt(LocalDateTime.now());
        detail.getFlavorDetails().add(flavorDetail);
    }
}