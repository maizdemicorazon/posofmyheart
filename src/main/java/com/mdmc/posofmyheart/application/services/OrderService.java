package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.domain.models.OrderRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    OrderEntity createOrder(OrderRequest request);

    List<OrderResponse> listOrdersByDate(LocalDate date);

    OrderResponse findOrderById(Long orderId);
}
