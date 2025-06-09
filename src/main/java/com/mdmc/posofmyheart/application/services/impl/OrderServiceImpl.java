package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.*;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import com.mdmc.posofmyheart.domain.patterns.strategies.impl.CreateOrderStrategy;
import com.mdmc.posofmyheart.domain.patterns.strategies.impl.UpdateOrderStrategy;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static com.mdmc.posofmyheart.util.DateTimeUtils.randomEveningDateTime;

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
    public List<OrderResponseCreate> findAllOrdersToCreate() {
        return findAllOrders()
                .stream()
                .map(OrderMapper.INSTANCE::toResponseCreate)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new OrderNotFoundException(orderId));
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
    public CreateOrderResponseDto createOrder(OrderRequest request, LocalDate date) {
        LocalDate localDate = Optional.ofNullable(date).orElseGet(LocalDate::now);
        LocalDateTime orderDate = randomEveningDateTime(localDate);
        return createOrderStrategy.execute(
                OrderRequest.builder()
                        .withIdPaymentMethod(request.idPaymentMethod())
                        .withClientName(request.clientName())
                        .withOrderDate(orderDate)
                        .withItems(request.items())
                        .build()
        );
    }

    @Override
    @Transactional
    public List<CreateOrderResponseDto> createOrders(List<OrderRequest> requests) {


        return requests.stream()
                .map(createOrderStrategy::execute
                )
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
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        orderRepository.delete(order);
    }

}
