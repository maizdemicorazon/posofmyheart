package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderRestore;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.domain.OrderStatusEnum;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponseDto;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponse> findAllOrders();

    OrderRestore findOrdersToBackup();

    List<OrderResponse> findOrdersByDate(LocalDate date);

    List<OrderResponse> findOrdersByPeriod(LocalDate start, LocalDate end);

    OrderResponse findOrderById(Long orderId);

    CreateOrderResponseDto createOrder(OrderRequest request);

    List<OrderRequest> restoreBackup(OrderRestore restore);

    /**
     * <bold>Notas importantes:</bold>
     * Para eliminar items: Simplemente no incluirlos en el array updatedItems
     * Para eliminar extras: Incluirlos con quantity: 0 o no incluirlos en updatedExtras
     * Para eliminar salsa: Usar "idSauce": null
     * Campos opcionales: Todos los campos son opcionales excepto:
     * idOrderDetail o (idProduct + idVariant) para items
     * idExtra para extras
     *
     * @param idOrder       - id order which belongs detail or extra
     * @param updateRequest - body to update
     * @return {@link OrderResponse}
     */
    OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest);

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "orders", key = "'allOrders'"),
            @CacheEvict(value = "orders", key = "'ordersWithDetails'"),
            @CacheEvict(value = "orders", key = "'backup'"),
            @CacheEvict(value = "orders", key = "'order-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'orderBasic-' + #idOrder"),
            @CacheEvict(value = "orders", key = "'ordersByPeriod'")
    })
    void updateStatus(Long idOrder, OrderStatusEnum status);

    void deleteOrder(Long idOrder);

}
