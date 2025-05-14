package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponse> findAllOrders();

    List<OrderResponse> listOrdersByDate(LocalDate date);

    OrderResponse findOrderById(Long orderId);

    CreateOrderResponse createOrder(OrderRequest request);
}
