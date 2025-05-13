package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.SauceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SauceRepository sauceRepository;

    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        return orderRepository.findByOrderDate(date).stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public OrderEntity createOrder(OrderRequest request) {
        // 1. Validar método de pago
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.paymentMethodId().longValue())
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no válido"));

        // 2. Crear la orden
        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setComment(request.comment());
        order.setOrderDetails(new ArrayList<>());

        // 3. Procesar items
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest item : request.items()) {
            ProductEntity product = productRepository.findById(item.idProduct().longValue())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.idProduct()));

            // Obtener la salsa (si se especifica)
            SauceEntity sauce = sauceRepository.findById(item.idSauce())
                    .orElseThrow(() -> new IllegalArgumentException("Salsa no encontrada: " + item.idSauce()));

//            Price price = Price.to(product.getPrices()).orElseThrow();

            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setOrder(order);
            detail.setProduct(product);
//            detail.setUnitPrice(price.sellPrice());
            detail.setSauce(sauce);

            order.getOrderDetails().add(detail);
        }

        // 4. Asignar total y guardar
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }
}