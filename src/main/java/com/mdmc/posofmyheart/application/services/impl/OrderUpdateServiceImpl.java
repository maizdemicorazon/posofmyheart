package com.mdmc.posofmyheart.application.services.impl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

import jakarta.transaction.Transactional;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.application.mappers.OrderResponseMapper;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.application.services.OrderUpdateService;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.domain.patterns.validator.OrderValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderExtraDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderFlavorDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;

@Service
@RequiredArgsConstructor
public class OrderUpdateServiceImpl implements OrderUpdateService {
    private final OrderRepository orderRepository;
    private final OrderCalculationService calculationService;
    private final OrderValidator orderValidator;
    private final EntityFinder entityFinder;

    @Transactional
    public OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest) {
        // Validar request de actualización
        orderValidator.validateOrderUpdate(updateRequest);

        // Buscar orden existente
        OrderEntity existingOrder = orderRepository.findById(idOrder)
                .orElseThrow(() -> new OrderNotFoundException(idOrder));

        // Aplicar actualizaciones
        updateOrderFields(existingOrder, updateRequest);

        // Recalcular total si es necesario
        if (hasItemChanges(updateRequest)) {
            existingOrder.setTotalAmount(calculationService.calculateOrderTotal(existingOrder));
        }

        // Actualizar timestamp
        existingOrder.setUpdatedAt(LocalDateTime.now());

        // Guardar y retornar
        OrderEntity updatedOrder = orderRepository.save(existingOrder);
        return OrderResponseMapper.INSTANCE.toResponse(updatedOrder);
    }

    private void updateOrderFields(OrderEntity order, OrderUpdateRequest updateRequest) {
        if (updateRequest.clientName() != null) {
            order.setClientName(updateRequest.clientName());
        }
        // Actualizar tipo de pago si está presente
        if (updateRequest.idPaymentMethod() != null) {
            PaymentMethodEntity newPaymentMethod = entityFinder
                    .findPaymentMethod(updateRequest.idPaymentMethod());
            order.setPaymentMethod(newPaymentMethod);
        }

        // Actualizar items si están presentes
        if (updateRequest.updatedItems() != null && !updateRequest.updatedItems().isEmpty()) {
            updateOrderItems(order, updateRequest.updatedItems());
        }
    }

    private boolean hasItemChanges(OrderUpdateRequest updateRequest) {
        return updateRequest.updatedItems() != null && !updateRequest.updatedItems().isEmpty();
    }

    private void updateOrderItems(OrderEntity order, List<OrderUpdateRequest.OrderItemUpdate> newItems) {
        // Limpiar items existentes
        order.getOrderDetails().clear();

        // Agregar nuevos items usando el mismo proceso que en creación
        newItems.forEach(item -> {
            OrderDetailEntity detail = createDetailFromItem(item);
            order.addOrderDetail(detail);
        });
    }

    private OrderDetailEntity createDetailFromItem(OrderUpdateRequest.OrderItemUpdate item) {
        ProductEntity product = entityFinder.findProduct(item.idProduct());
        ProductVariantEntity variant = entityFinder.findVariant(item.idVariant());

        OrderDetailEntity detail = OrderDetailEntity.builder()
                .product(product)
                .variant(variant)
                .comment(item.comment())
                .sellPrice(variant.getActualSellPrice())
                .productionCost(variant.getActualCostPrice())
                .build();

        // Procesar extras, salsas y sabores aquí también
        processItemExtras(detail, item);

        return detail;
    }

    private void processItemExtras(OrderDetailEntity detail, OrderUpdateRequest.OrderItemUpdate item) {
        // Procesar extras
        Optional.ofNullable(item.updatedExtras())
                .orElseGet(Collections::emptyList)
                .forEach(extra -> {
                    ProductExtraEntity productExtra = entityFinder.findProductExtra(extra.idExtra());

                    OrderExtraDetailEntity extraDetail = OrderExtraDetailEntity.builder()
                            .quantity(extra.quantity())
                            .sellPrice(productExtra.getActualPrice())
                            .productionCost(productExtra.getActualCost())
                            .build();

                    extraDetail.setRelations(detail, productExtra);
                    detail.addExtraDetail(extraDetail);
                });

        // Procesar salsas
        Optional.ofNullable(item.updatedSauces())
                .orElseGet(Collections::emptyList)
                .forEach(sauce -> {
                    ProductSauceEntity productSauceEntity = entityFinder.findSauce(sauce.idSauce());
                    detail.addSauce(productSauceEntity);
                });

        // Procesar sabores
        Optional.ofNullable(item.updatedFlavor())
                .ifPresent(flavor -> {
                    ProductFlavorEntity flavorEntity = entityFinder.findFlavor(flavor.idFlavor());
                    OrderFlavorDetailEntity flavorDetail = new OrderFlavorDetailEntity(detail, flavorEntity);
                    flavorDetail.setCreatedAt(LocalDateTime.now());
                    detail.getFlavorDetails().add(flavorDetail);
                });
    }
}
