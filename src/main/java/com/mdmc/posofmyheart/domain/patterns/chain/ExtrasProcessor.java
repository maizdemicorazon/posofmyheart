package com.mdmc.posofmyheart.domain.patterns.chain;

import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;
import com.mdmc.posofmyheart.api.exceptions.ProductExtraNotFoundException;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Component
public class ExtrasProcessor extends OrderItemProcessor {

    @Override
    public void process(OrderDetailEntity detail, OrderItemRequest item, PreloadedEntities preloaded) {
        Optional.ofNullable(item.extras())
                .orElseGet(Collections::emptyList)
                .forEach(extra -> addExtraToDetail(detail, extra, preloaded.extras()));

        processNext(detail, item, preloaded);
    }

    private void addExtraToDetail(OrderDetailEntity detail, OrderExtrasDetail extra, Map<Long, ProductExtraEntity> extras) {
        ProductExtraEntity productExtra = Optional.ofNullable(extras.get(extra.idExtra()))
                .orElseThrow(() -> new ProductExtraNotFoundException(extra.idExtra()));

        OrderExtraDetailEntity extraDetail = new OrderExtraDetailEntity();
        extraDetail.setRelations(detail, productExtra);
        extraDetail.setQuantity(extra.quantity());
        extraDetail.setSellPrice(productExtra.getActualPrice());
        extraDetail.setProductionCost(productExtra.getActualCost());
        detail.getExtraDetails().add(extraDetail);
    }
}