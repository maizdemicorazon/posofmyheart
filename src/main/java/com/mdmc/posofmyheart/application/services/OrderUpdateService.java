package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.OrderResponse;
import com.mdmc.posofmyheart.application.dtos.OrderUpdateRequest;

public interface OrderUpdateService {
    OrderResponse updateOrder(Long idOrder, OrderUpdateRequest updateRequest);
}
