package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.OrderNotFoundException;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.application.mappers.OrderMapper;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.application.services.OrderUpdateService;
import com.mdmc.posofmyheart.domain.patterns.facade.EntityFinder;
import com.mdmc.posofmyheart.domain.patterns.validator.OrderValidator;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.*;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
                .orElseThrow(OrderNotFoundException::new);

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
        return OrderMapper.INSTANCE.toResponse(updatedOrder);
    }

    private void updateOrderFields(OrderEntity order, OrderUpdateRequest updateRequest) {
        // Actualizar comentario si está presente
        if (updateRequest.comment() != null) {
            order.setComment(updateRequest.comment());
        }

        // Actualizar método de pago si está presente
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

                    OrderExtrasDetailEntity extraDetail = OrderExtrasDetailEntity.builder()
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
                    SauceEntity sauceEntity = entityFinder.findSauce(sauce.idSauce());
                    detail.addSauce(sauceEntity);
                });

        // Procesar sabores
        Optional.ofNullable(item.updatedFlavors())
                .filter(list -> !list.isEmpty())
                .ifPresent(flavors -> {
                    if (flavors.size() > 1) {
                        throw new IllegalArgumentException("Solo se puede seleccionar un sabor por item");
                    }

                    ProductFlavorEntity flavorEntity = entityFinder.findFlavor(flavors.get(0).idFlavor());
                    OrderFlavorDetailEntity flavorDetail = new OrderFlavorDetailEntity(detail, flavorEntity);
                    flavorDetail.setCreatedAt(LocalDateTime.now());
                    detail.getFlavorDetails().add(flavorDetail);
                });
    }

    private boolean hasItemChanges(OrderUpdateRequest updateRequest) {
        return updateRequest.updatedItems() != null && !updateRequest.updatedItems().isEmpty();
    }
}
