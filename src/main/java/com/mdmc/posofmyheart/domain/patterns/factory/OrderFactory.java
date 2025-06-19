package com.mdmc.posofmyheart.domain.patterns.factory;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.domain.patterns.builder.OrderBuilder;
import com.mdmc.posofmyheart.domain.patterns.chain.OrderItemProcessorChain;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.domain.patterns.facade.PreloadedEntities;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Optional;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class OrderFactory {
    private final OrderBuilder orderBuilder;
    private final OrderItemProcessorChain processorChain;
    private final EntityFinder entityFinder;

    public OrderEntity createOrder(OrderRequest request) {
        OrderEntity order = orderBuilder.buildFromRequest(request);

        Set<Long> productIds = request.items().stream()
                .map(OrderItemRequest::idProduct)
                .collect(Collectors.toSet());
        Set<Long> variantIds = request.items().stream()
                .map(OrderItemRequest::idVariant)
                .collect(Collectors.toSet());
        Set<Long> extraIds = request.items().stream()
                .flatMap(i -> Optional.ofNullable(i.extras()).orElse(Collections.emptyList()).stream())
                .map(e -> e.idExtra())
                .collect(Collectors.toSet());
        Set<Long> sauceIds = request.items().stream()
                .flatMap(i -> Optional.ofNullable(i.sauces()).orElse(Collections.emptyList()).stream())
                .map(s -> s.idSauce())
                .collect(Collectors.toSet());
        Set<Long> flavorIds = request.items().stream()
                .map(OrderItemRequest::flavor)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Long, ProductEntity> products = entityFinder.findProducts(productIds);
        Map<Long, ProductVariantEntity> variants = entityFinder.findVariants(variantIds);
        Map<Long, ProductExtraEntity> extras = entityFinder.findProductExtras(extraIds);
        Map<Long, ProductSauceEntity> sauces = entityFinder.findSauces(sauceIds);
        Map<Long, ProductFlavorEntity> flavors = entityFinder.findFlavors(flavorIds);

        PreloadedEntities preloaded = new PreloadedEntities(extras, sauces, flavors);

        request.items().forEach(item -> {
            OrderDetailEntity detail = orderBuilder.buildDetail(item, products, variants);
            processorChain.process(detail, item, preloaded);
            detail.setOrder(order);
            order.addOrderDetail(detail);
        });

        return order;
    }
}
