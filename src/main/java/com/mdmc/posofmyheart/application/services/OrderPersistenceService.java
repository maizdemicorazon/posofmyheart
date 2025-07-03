package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;

public interface OrderPersistenceService {
    CreateOrderResponseDto saveOrder(OrderEntity order);
    OrderRequest saveOrderBackup(OrderEntity order);
}
