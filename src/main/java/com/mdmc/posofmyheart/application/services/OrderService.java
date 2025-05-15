package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderRequest;
import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;
import com.mdmc.posofmyheart.domain.dtos.CreateOrderResponse;

import java.time.LocalDate;
import java.util.List;

public interface OrderService {
    List<OrderResponse> findAllOrders();

    List<OrderResponse> listOrdersByDate(LocalDate date);

    OrderResponse findOrderById(Long orderId);

    CreateOrderResponse createOrder(OrderRequest request);

    /**
     * <bold>Notas importantes:</bold>
     * Para eliminar items: Simplemente no incluirlos en el array updatedItems
     * Para eliminar extras: Incluirlos con quantity: 0 o no incluirlos en updatedExtras
     * Para eliminar salsa: Usar "idSauce": null
     * Campos opcionales: Todos los campos son opcionales excepto:
     * idOrderDetail o (idProduct + idVariant) para items
     * idExtra para extras
     *
     * @param idOrder - id order which belongs detail or extra
     * @param updateRequest - body to update
     * @return {@link OrderResponse}
     */
    OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest);
}
