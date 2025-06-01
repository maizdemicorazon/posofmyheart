package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.application.dtos.UpdateOrderData;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.domain.patterns.strategies.impl.CreateOrderStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.impl.UpdateOrderStrategy;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final CreateOrderStrategy createOrderStrategy;
    private final UpdateOrderStrategy updateOrderStrategy;

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::toResponse)
                .orElseThrow(OrderNotFoundException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        return orderRepository.findByOrderDate(date)
                .stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CreateOrderResponse createOrder(OrderRequest request) {
        return createOrderStrategy.execute(request);
    }

    @Override
    @Transactional
    public List<CreateOrderResponse> createOrders(List<OrderRequest> requests) {
        return requests.stream()
                .map(createOrderStrategy::execute)
                .toList();
    }

    @Override
    @Transactional
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        UpdateOrderData updateData = new UpdateOrderData(idOrder, updateRequest);
        return updateOrderStrategy.execute(updateData);
    }

    @Override
    @Transactional
    public void deleteOrder(Long idOrder) {
        OrderEntity order = orderRepository.findById(idOrder)
                .orElseThrow(OrderNotFoundException::new);

        orderRepository.delete(order);
    }
}
