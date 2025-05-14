package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.ResourceNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.domain.models.ProductExtrasDetail;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SauceRepository sauceRepository;
    private final VariantRepository variantRepository;

    @Override
    public List<OrderResponse> findAllOrders() {
        return orderRepository.findAll()
                .stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public OrderResponse findOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .map(OrderMapper.INSTANCE::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> listOrdersByDate(LocalDate date) {
        return orderRepository.findByOrderDate(date)
                .stream()
                .map(OrderMapper.INSTANCE::toResponse)
                .toList();
    }

    @Transactional
    public CreateOrderResponse createOrder(OrderRequest request) {
        OrderEntity order = createOrderFromRequest(request);
        createOrderAndExtrasDetailFromRequest(request, order);
        return new CreateOrderResponse(
                orderRepository.save(order).getIdOrder()
        );
    }

    private void createOrderAndExtrasDetailFromRequest(OrderRequest request, OrderEntity order) {
        request.items().forEach(item -> {
            OrderDetailEntity detail = createAndAddDetail(order, item);
            item.extras().forEach(extra -> createAndAddExtraDetail(detail, extra));
        });
    }

    private OrderDetailEntity createAndAddDetail(OrderEntity order, OrderItemRequest item) {
        OrderDetailEntity detail = createOrderDetail(item);
        order.addOrderDetail(detail);
        return detail;
    }

    private void createAndAddExtraDetail(OrderDetailEntity detail, ProductExtrasDetail extra) {
        ProductExtraEntity productExtra = productExtraRepository.findById(extra.idExtra())
                .orElseThrow(() -> new ResourceNotFoundException("Extra no encontrado"));

        ProductExtrasDetailEntity extraDetail = new ProductExtrasDetailEntity();
        extraDetail.setQuantity(extra.quantity());
        extraDetail.setRelations(detail, productExtra);
        detail.addExtraDetail(extraDetail);
    }

    private OrderEntity createOrderFromRequest(OrderRequest request) {
        // 1. Validar método de pago
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.idPaymentMethod())
                .orElseThrow(() -> new ResourceNotFoundException("Método de pago no válido"));

        // 2. Crear la orden
        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setComment(request.comment());
        order.setTotalAmount(request.amount());
        return order;
    }

    private OrderDetailEntity createOrderDetail(OrderItemRequest item) {
        ProductEntity product = productRepository.findById(item.idProduct())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        SauceEntity sauce = item.idSauce() != null ?
                sauceRepository.findById(item.idSauce())
                        .orElseThrow(() -> new ResourceNotFoundException("Salsa no encontrada")) :
                null;

        ProductVariantEntity variant = variantRepository.findById(item.idVariant())
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada"));

        OrderDetailEntity detail = new OrderDetailEntity();
        detail.setProduct(product);
        detail.setSauce(sauce);
        detail.setVariant(variant);

        return detail;
    }

}