package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.domain.models.ProductExtrasDetail;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private final VariantRepository variantRepository;

    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Integer orderId) {
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
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.idPaymentMethod())
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no válido"));

        // 2. Crear la orden
        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setComment(request.comment());
        order.setOrderDetails(new ArrayList<>());
        order.setTotalAmount(request.amount());

        OrderEntity savedOrder = orderRepository.save(order);

        createDetails(request.items(), savedOrder);

        return savedOrder;
    }

    private void createDetails(List<OrderItemRequest> items, OrderEntity order){
        // 3. Procesar items
        for (OrderItemRequest item : items) {
            ProductEntity product = productRepository.findById(item.idProduct())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.idProduct()));

            // Obtener la salsa
            SauceEntity sauce = sauceRepository.findById(item.idSauce())
                    .orElseThrow(() -> new IllegalArgumentException("Salsa no encontrada: " + item.idSauce()));

            // Obtener la salsa (si se especifica)
            ProductVariantEntity variant = variantRepository.findById(item.idVariant())
                    .orElseThrow(() -> new IllegalArgumentException("Tamaño no encontrado: " + item.idVariant()));

            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setSauce(sauce);
            detail.setVariant(variant);

            item.extras().forEach(extra -> {
                ProductExtrasDetailEntity extraDetail = new ProductExtrasDetailEntity();
//                extraDetail.setIdExtra(extra.idExtra());
                extraDetail.setQuantity(extra.quantity());
            });

            order.getOrderDetails().add(detail);

        }
    }
}