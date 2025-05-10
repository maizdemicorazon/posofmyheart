package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.models.OrderItemRequest;
import com.mdmc.posofmyheart.domain.models.OrderRequest;
import com.mdmc.posofmyheart.domain.models.Price;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final PaymentMethodRepository paymentMethodRepository;

    public OrderEntity createOrder(OrderRequest request) {
        // 1. Validar método de pago
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.paymentMethodId())
                .orElseThrow(() -> new IllegalArgumentException("Método de pago no válido"));

        // 2. Crear la orden
        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setNotes(request.notes());
        order.setDetails(new ArrayList<>());

        // 3. Procesar items
        BigDecimal total = BigDecimal.ZERO;
        for (OrderItemRequest item : request.items()) {
            ProductEntity product = productRepository.findById(item.productId())
                    .orElseThrow(() -> new IllegalArgumentException("Producto no encontrado: " + item.productId()));

            Price price = Price.to(product.getPrices()).orElseThrow();

            OrderDetailEntity detail = new OrderDetailEntity();
            detail.setOrder(order);
            detail.setProduct(product);
            detail.setQuantity(item.quantity());
            detail.setUnitPrice(price.sellPrice());
            detail.setUnitCost(price.costPrice());

            order.getDetails().add(detail);
            total = total.add(detail.getUnitPrice().multiply(BigDecimal.valueOf(item.quantity())));
        }

        // 4. Asignar total y guardar
        order.setTotalAmount(total);
        return orderRepository.save(order);
    }
}