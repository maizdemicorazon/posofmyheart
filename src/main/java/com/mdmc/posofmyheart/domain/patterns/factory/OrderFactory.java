package com.mdmc.posofmyheart.domain.patterns.factory;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.patterns.builder.OrderBuilder;
import com.mdmc.posofmyheart.domain.patterns.chain.OrderItemProcessorChain;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OrderFactory {
    private final OrderBuilder orderBuilder;
    private final OrderItemProcessorChain processorChain;

    public OrderEntity createOrder(OrderRequest request) {
        OrderEntity order = orderBuilder.buildFromRequest(request);

        request.items().forEach(item -> {
            OrderDetailEntity detail = orderBuilder.buildDetail(item);
            processorChain.process(detail, item);
            detail.setOrder(order);
            order.addOrderDetail(detail);
        });

        return order;
    }
}
