package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.*;
import com.mdmc.posofmyheart.application.dtos.OrderItemRequest;
import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final ProductExtraRepository productExtraRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final SauceRepository sauceRepository;
    private final ProductVariantRepository variantRepository;

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
                .orElseThrow(OrderNotFoundException::new);
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

    private OrderEntity createOrderFromRequest(OrderRequest request) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.idPaymentMethod())
                .orElseThrow(PayMethodNotFoundException::new);

        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setComment(request.comment());
        order.setTotalAmount(request.amount());
        return order;
    }

    private void createOrderAndExtrasDetailFromRequest(OrderRequest request, OrderEntity order) {
        request.items().forEach(item -> {
            OrderDetailEntity detail = createAndAddDetail(order, item);
            Optional.ofNullable(item.extras())
                    .orElseGet(Collections::emptyList)
                    .forEach(extra -> createAndAddExtraDetail(detail, extra));
        });
    }


    private OrderDetailEntity createNewOrderDetail(OrderEntity order, OrderUpdateRequest.OrderItemUpdate itemUpdate) {
        // Validar datos requeridos
        if (itemUpdate.idProduct() == null || itemUpdate.idVariant() == null) {
            throw new IllegalArgumentException("Detalle del producto y variante son requeridos para nuevos items");
        }

        // Obtener entidades relacionadas
        ProductEntity product = productRepository.findById(itemUpdate.idProduct())
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + itemUpdate.idProduct()));

        ProductVariantEntity variant = variantRepository.findById(itemUpdate.idVariant())
                .orElseThrow(() -> new ResourceNotFoundException("Variante no encontrada con ID: " + itemUpdate.idVariant()));

        // Validar que la variante pertenece al producto
        if (!variant.getProduct().getIdProduct().equals(product.getIdProduct())) {
            throw new IllegalArgumentException("La variante no corresponde al producto especificado");
        }

        // Crear el nuevo detalle
        OrderDetailEntity newDetail = new OrderDetailEntity();
        newDetail.setOrder(order);
        newDetail.setProduct(product);
        newDetail.setVariant(variant);

        // Manejar salsa (opcional)
        if (itemUpdate.idSauce() != null) {
            SauceEntity sauce = sauceRepository.findById(itemUpdate.idSauce())
                    .orElseThrow(() -> new ResourceNotFoundException("Salsa no encontrada con ID: " + itemUpdate.idSauce()));
            newDetail.setSauce(sauce);
        }

        // Manejar extras si existen
        if (itemUpdate.updatedExtras() != null && !itemUpdate.updatedExtras().isEmpty()) {
            itemUpdate.updatedExtras().forEach(extraUpdate -> {
                ProductExtraEntity productExtra = productExtraRepository.findById(extraUpdate.idExtra())
                        .orElseThrow(() -> new ResourceNotFoundException("Extra no encontrado con ID: " + extraUpdate.idExtra()));

                OrderExtrasDetailEntity extraDetail = new OrderExtrasDetailEntity();
                extraDetail.setQuantity(extraUpdate.quantity());
                extraDetail.setRelations(newDetail, productExtra);
                newDetail.addExtraDetail(extraDetail);
            });
        }

        return newDetail;
    }


    private OrderDetailEntity createAndAddDetail(OrderEntity order, OrderItemRequest item) {
        OrderDetailEntity detail = createOrderDetail(item);
        order.addOrderDetail(detail);
        return detail;
    }

    private void createAndAddExtraDetail(OrderDetailEntity detail, OrderExtrasDetail extra) {
        ProductExtraEntity productExtra = productExtraRepository.findById(extra.idExtra())
                .orElseThrow(ProductExtraNotFoundException::new);

        OrderExtrasDetailEntity extraDetail = new OrderExtrasDetailEntity();
        extraDetail.setQuantity(extra.quantity());
        extraDetail.setRelations(detail, productExtra);
        detail.addExtraDetail(extraDetail);
    }

    private OrderDetailEntity createOrderDetail(OrderItemRequest item) {
        ProductEntity product = productRepository.findById(item.idProduct())
                .orElseThrow(ProductNotFoundException::new);

        SauceEntity sauce = item.idSauce() != null ?
                sauceRepository.findById(item.idSauce())
                        .orElseThrow(SauceNotFoundException::new) :
                null;

        ProductVariantEntity variant = variantRepository.findById(item.idVariant())
                .orElseThrow(VariantNotFoundException::new);

        OrderDetailEntity detail = new OrderDetailEntity();
        detail.setProduct(product);
        detail.setSauce(sauce);
        detail.setVariant(variant);

        return detail;
    }

    @Transactional
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        // 1. Buscar la orden existente
        OrderEntity existingOrder = orderRepository.findById(idOrder)
                .orElseThrow(OrderNotFoundException::new);

        // 2. Validar y actualizar datos básicos
        if (updateRequest.comment() != null) {
            existingOrder.setComment(updateRequest.comment());
        }

        if (updateRequest.idPaymentMethod() != null) {
            PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(updateRequest.idPaymentMethod())
                    .orElseThrow(PayMethodNotFoundException::new);
            existingOrder.setPaymentMethod(paymentMethod);
        }

        // 3. Procesar actualización de items
        if (updateRequest.updatedItems() != null && !updateRequest.updatedItems().isEmpty()) {
            updateOrderItems(existingOrder, updateRequest.updatedItems());
        }

        // 4. Recalcular el total
        BigDecimal newTotal = calculateOrderTotal(existingOrder);
        existingOrder.setTotalAmount(newTotal);

        // 5. Guardar y retornar
        OrderEntity updatedOrder = orderRepository.save(existingOrder);
        return OrderMapper.INSTANCE.toResponse(updatedOrder);
    }

    private void updateOrderItems(OrderEntity order, List<OrderUpdateRequest.OrderItemUpdate> updatedItems) {
        // Eliminar items que ya no están en la orden actualizada
        order.getOrderDetails().removeIf(detail ->
                updatedItems.stream().noneMatch(item -> item.idOrderDetail().equals(detail.getIdOrderDetail())));

        // Actualizar o añadir items
        updatedItems.forEach(itemUpdate -> {
            if (itemUpdate.idOrderDetail() != null) {
                // Actualizar item existente
                OrderDetailEntity existingDetail = order.getOrderDetails().stream()
                        .filter(d -> d.getIdOrderDetail().equals(itemUpdate.idOrderDetail()))
                        .findFirst()
                        .orElseThrow(OrderDetailNotFoundException::new);

                updateExistingOrderDetail(existingDetail, itemUpdate);
            } else {
                // Añadir nuevo item
                OrderDetailEntity newDetail = createNewOrderDetail(order, itemUpdate);
                order.addOrderDetail(newDetail);
            }
        });
    }

    private void updateExistingOrderDetail(OrderDetailEntity detail, OrderUpdateRequest.OrderItemUpdate itemUpdate) {
        if (itemUpdate.idProduct() != null) {
            ProductEntity product = productRepository.findById(itemUpdate.idProduct())
                    .orElseThrow(ProductNotFoundException::new);
            detail.setProduct(product);
        }

        if (itemUpdate.idVariant() != null) {
            ProductVariantEntity variant = variantRepository.findById(itemUpdate.idVariant())
                    .orElseThrow(VariantNotFoundException::new);
            detail.setVariant(variant);
        }

        if (itemUpdate.idSauce() != null) {
            SauceEntity sauce = sauceRepository.findById(itemUpdate.idSauce())
                    .orElseThrow(SauceNotFoundException::new);
            detail.setSauce(sauce);
        }

        // Actualizar extras
        if (itemUpdate.updatedExtras() != null) {
            updateExtrasForDetail(detail, itemUpdate.updatedExtras());
        }
    }

    private void updateExtrasForDetail(OrderDetailEntity detail, List<OrderUpdateRequest.ProductExtraUpdate> extrasUpdate) {
        // Eliminar extras que ya no están
        detail.getExtraDetails().removeIf(extra ->
                extrasUpdate.stream().noneMatch(e -> e.idExtra().equals(extra.getProductExtra().getIdExtra())));

        // Actualizar o añadir extras
        extrasUpdate.forEach(extraUpdate -> {
            ProductExtraEntity productExtra = productExtraRepository.findById(extraUpdate.idExtra())
                    .orElseThrow(ProductExtraNotFoundException::new);

            detail.getExtraDetails().stream()
                    .filter(e -> e.getProductExtra().getIdExtra().equals(extraUpdate.idExtra()))
                    .findFirst()
                    .ifPresentOrElse(
                            existingExtra -> existingExtra.setQuantity(extraUpdate.quantity()),
                            () -> {
                                OrderExtrasDetailEntity newExtra = new OrderExtrasDetailEntity();
                                newExtra.setQuantity(extraUpdate.quantity());
                                newExtra.setRelations(detail, productExtra);
                                detail.addExtraDetail(newExtra);
                            }
                    );
        });
    }

    private BigDecimal calculateOrderTotal(OrderEntity order) {
        BigDecimal total = BigDecimal.ZERO;

        for (OrderDetailEntity detail : order.getOrderDetails()) {
            BigDecimal itemTotal = detail.getVariant().getSellPrice();

            // Sumar extras
            BigDecimal extrasTotal = detail.getExtraDetails().stream()
                    .map(e -> e.getProductExtra().getPrice().multiply(BigDecimal.valueOf(e.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            total = total.add(itemTotal.add(extrasTotal));
        }

        return total;
    }

}