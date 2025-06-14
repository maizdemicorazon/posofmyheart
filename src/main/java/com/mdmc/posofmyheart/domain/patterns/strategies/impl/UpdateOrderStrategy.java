package com.mdmc.posofmyheart.domain.patterns.strategies.impl;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.UpdateOrderData;
import com.mdmc.posofmyheart.application.services.OrderUpdateService;
import com.mdmc.posofmyheart.domain.patterns.strategies.OrderOperationStrategy;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class UpdateOrderStrategy implements OrderOperationStrategy<UpdateOrderData, OrderResponse> {
    private final OrderUpdateService updateService;

    @Override
    public OrderResponse execute(UpdateOrderData updateData) {
        return updateService.updateOrder(updateData.orderId(), updateData.updateRequest());
    }
}
