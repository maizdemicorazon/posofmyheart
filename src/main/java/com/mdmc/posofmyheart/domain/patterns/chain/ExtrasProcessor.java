package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductExtraEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class ExtrasProcessor extends OrderItemProcessor {
    private final EntityFinder entityFinder;

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item) {
        Optional.ofNullable(item.extras())
                .orElseGet(Collections::emptyList)
                .forEach(extra -> addExtraToDetail(detail, extra));

        processNext(detail, item);
    }

    private void addExtraToDetail(OrderDetailEntity detail, OrderExtrasDetail extra) {
        ProductExtraEntity productExtra = entityFinder.findProductExtra(extra.idExtra());

        OrderExtraDetailEntity extraDetail = new OrderExtraDetailEntity();
        extraDetail.setRelations(detail, productExtra);
        extraDetail.setQuantity(extra.quantity());
        extraDetail.setSellPrice(productExtra.getActualPrice());
        extraDetail.setProductionCost(productExtra.getActualCost());
        detail.getExtraDetails().add(extraDetail);
    }
}