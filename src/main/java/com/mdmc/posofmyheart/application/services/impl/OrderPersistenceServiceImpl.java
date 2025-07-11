package com.mdmc.posofmyheart.application.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.mappers.OrderRequestMapper;
import com.mdmc.posofmyheart.application.mappers.OrderResponseMapper;
import com.mdmc.posofmyheart.application.services.OrderPersistenceService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderPersistenceServiceImpl implements OrderPersistenceService {
    private final OrderRepository orderRepository;

    public List<CreateOrderResponseDto> saveOrders(List<OrderEntity> orders) {
        return orders.stream()
                .map(this::saveOrder)
                .toList();
    }

    public CreateOrderResponseDto saveOrder(OrderEntity order) {
        return new CreateOrderResponseDto(
                orderRepository.save(order).getIdOrder()
        );
    }

    @Override
    public OrderRequest saveOrderBackup(OrderEntity order) {
        return OrderRequestMapper.INSTANCE
                .toOrderRequest(
                        OrderResponseMapper.INSTANCE
                                .toResponse(orderRepository.save(order)
                                )
                );
    }
}
