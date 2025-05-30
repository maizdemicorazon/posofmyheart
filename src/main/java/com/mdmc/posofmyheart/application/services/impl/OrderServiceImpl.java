package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.*;
import com.mdmc.posofmyheart.application.dtos.*;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderService;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;
import com.mdmc.posofmyheart.domain.models.OrderExtrasDetail;
import com.mdmc.posofmyheart.domain.models.Sauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

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
        // Validación básica
        if (request.idPaymentMethod() == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Payment method and at least one item are required");
        }

        // Crear la orden base
        OrderEntity order = createOrderFromRequest(request);

        // Procesar items y sus relaciones
        request.items().forEach(item -> {
            OrderDetailEntity detail = createAndAddDetail(order, item);

            // Procesar extras del item
            Optional.ofNullable(item.extras())
                    .orElseGet(Collections::emptyList)
                    .forEach(extra -> createAndAddExtraDetail(detail, extra));

            // Procesar salsas del item
            Optional.ofNullable(item.sauces())
                    .orElseGet(Collections::emptyList)
                    .forEach(sauce -> addSauceToDetail(detail, sauce));
        });

        // Calcular y establecer el total
        order.setTotalAmount(calculateOrderTotal(order));

        // Guardar y retornar respuesta
        return new CreateOrderResponse(orderRepository.save(order).getIdOrder());
    }

    @Transactional
    public List<CreateOrderResponse> createOrders(List<OrderRequest> requests) {
        List<CreateOrderResponse> responses = new LinkedList<>();

        requests.forEach(request -> {
        // Validación básica
        if (request.idPaymentMethod() == null || request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("Payment method and at least one item are required");
        }

        // Crear la orden base
        OrderEntity order = createOrderFromRequest(request);

        // Procesar items y sus relaciones
        request.items().forEach(item -> {
            OrderDetailEntity detail = createAndAddDetail(order, item);

            // Procesar extras del item
            Optional.ofNullable(item.extras())
                    .orElseGet(Collections::emptyList)
                    .forEach(extra -> createAndAddExtraDetail(detail, extra));

            // Procesar salsas del item
            Optional.ofNullable(item.sauces())
                    .orElseGet(Collections::emptyList)
                    .forEach(sauce -> addSauceToDetail(detail, sauce));
        });

        // Calcular y establecer el total
        order.setTotalAmount(calculateOrderTotal(order));

            responses.add(new CreateOrderResponse(orderRepository.save(order).getIdOrder()));

        });
        // Guardar y retornar respuesta
        return responses;
    }

    private void addSauceToDetail(OrderDetailEntity detail, Sauce sauce) {
        SauceEntity sauceEntity = sauceRepository.findById(sauce.idSauce())
                .orElseThrow(SauceNotFoundException::new);
        detail.addSauce(sauceEntity);
    }

    @Transactional
    public void deleteOrder(Long idOrder) {
        OrderEntity order = orderRepository.findById(idOrder)
                .orElseThrow(OrderNotFoundException::new);

        orderRepository.delete(order);
    }

    private OrderEntity createOrderFromRequest(OrderRequest request) {
        PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(request.idPaymentMethod())
                .orElseThrow(PayMethodNotFoundException::new);

        OrderEntity order = new OrderEntity();
        order.setPaymentMethod(paymentMethod);
        order.setComment(request.comment());
        order.setOrderDate(Objects.isNull(request.orderDate()) ? LocalDateTime.now() : request.orderDate());
        return order;
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

        // Manejar salsas (ahora soporta múltiples con cantidad)
        if (itemUpdate.updatedSauces() != null && !itemUpdate.updatedSauces().isEmpty()) {
            itemUpdate.updatedSauces().forEach(sauceUpdate -> {
                if (sauceUpdate.idSauce() > 0) { // Solo agregar si la cantidad es positiva
                    SauceEntity sauce = sauceRepository.findById(sauceUpdate.idSauce())
                            .orElseThrow(() -> new ResourceNotFoundException("Salsa no encontrada con ID: " + sauceUpdate.idSauce()));
                    newDetail.addSauce(sauce);
                }
            });
        }

        // Manejar extras
        if (itemUpdate.updatedExtras() != null && !itemUpdate.updatedExtras().isEmpty()) {
            itemUpdate.updatedExtras().forEach(extraUpdate -> {
                if (extraUpdate.quantity() > 0) { // Solo agregar si la cantidad es positiva
                    ProductExtraEntity productExtra = productExtraRepository.findById(extraUpdate.idExtra())
                            .orElseThrow(() -> new ResourceNotFoundException("Extra no encontrado con ID: " + extraUpdate.idExtra()));

                    OrderExtrasDetailEntity extraDetail = new OrderExtrasDetailEntity();
                    extraDetail.setQuantity(extraUpdate.quantity());
                    extraDetail.setRelations(newDetail, productExtra);
                    newDetail.addExtraDetail(extraDetail);
                }
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
        extraDetail.setSellPrice(productExtra.getActualPrice());
        extraDetail.setProductionCost(productExtra.getActualCost());
        extraDetail.setRelations(detail, productExtra);
        detail.addExtraDetail(extraDetail);
    }

    private OrderDetailEntity createOrderDetail(OrderItemRequest item) {
        ProductEntity product = productRepository.findById(item.idProduct())
                .orElseThrow(ProductNotFoundException::new);

        ProductVariantEntity variant = variantRepository.findById(item.idVariant())
                .orElseThrow(VariantNotFoundException::new);

        OrderDetailEntity detail = new OrderDetailEntity();
        detail.setProduct(product);
        detail.setVariant(variant);
        detail.setSellPrice(variant.getActualSellPrice());
        detail.setProductionCost(variant.getActualCostPrice());

        return detail;
    }


    @Transactional
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        OrderEntity existingOrder = orderRepository.findById(idOrder)
                .orElseThrow(OrderNotFoundException::new);

        // Actualizar campos básicos
        if (updateRequest.comment() != null) {
            existingOrder.setComment(updateRequest.comment());
        }

        if (updateRequest.idPaymentMethod() != null) {
            PaymentMethodEntity paymentMethod = paymentMethodRepository.findById(updateRequest.idPaymentMethod())
                    .orElseThrow(PayMethodNotFoundException::new);
            existingOrder.setPaymentMethod(paymentMethod);
        }

        // Procesar actualización de items
        if (updateRequest.updatedItems() != null && !updateRequest.updatedItems().isEmpty()) {
            updateOrderItems(existingOrder, updateRequest.updatedItems());
        }

        // Recalcular total
        existingOrder.setTotalAmount(calculateOrderTotal(existingOrder));

        return OrderMapper.INSTANCE.toResponse(orderRepository.save(existingOrder));
    }

    private void updateOrderItems(OrderEntity order, List<OrderUpdateRequest.OrderItemUpdate> updatedItems) {
        // Eliminar items que no están en la solicitud
        order.getOrderDetails().removeIf(detail ->
                updatedItems.stream().noneMatch(item -> item.idOrderDetail().equals(detail.getIdOrderDetail())));

        // Procesar items actualizados
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
        // Actualizar producto si es necesario
        if (itemUpdate.idProduct() != null) {
            ProductEntity product = productRepository.findById(itemUpdate.idProduct())
                    .orElseThrow(ProductNotFoundException::new);
            detail.setProduct(product);
        }

        // Actualizar variante si es necesario
        if (itemUpdate.idVariant() != null) {
            ProductVariantEntity variant = variantRepository.findById(itemUpdate.idVariant())
                    .orElseThrow(VariantNotFoundException::new);
            detail.setVariant(variant);
        }

        // Actualizar extras
        if (itemUpdate.updatedExtras() != null) {
            updateExtrasForDetail(detail, itemUpdate.updatedExtras());
        }

        // Actualizar salsas
        if (itemUpdate.updatedSauces() != null) {
            updateSaucesForDetail(detail, itemUpdate.updatedSauces());
        }
    }

    private void updateSaucesForDetail(OrderDetailEntity detail, List<OrderUpdateRequest.SauceUpdate> saucesUpdate) {
        // Limpiar salsas existentes
        detail.clearSauces();

        // Añadir las nuevas salsas
        saucesUpdate.forEach(sauceUpdate -> {
            if (sauceUpdate.idSauce() > 0) {
                SauceEntity sauce = sauceRepository.findById(sauceUpdate.idSauce())
                        .orElseThrow(SauceNotFoundException::new);
                detail.addSauce(sauce);
            }
        });
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
            // Precio base de la variante
            BigDecimal itemTotal = detail.getVariant().getActualSellPrice();

            // Sumar extras
            BigDecimal extrasTotal = detail.getExtraDetails().stream()
                    .map(e -> e.getProductExtra().getActualPrice().multiply(BigDecimal.valueOf(e.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            total = total.add(itemTotal.add(extrasTotal));
        }

        return total;
    }

}