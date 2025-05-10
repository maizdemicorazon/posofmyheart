package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.OrderRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

public interface OrderService {
    OrderEntity createOrder(OrderRequest request);
}
