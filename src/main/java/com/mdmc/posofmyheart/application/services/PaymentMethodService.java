package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;

import java.util.List;

public interface PaymentMethodService {

    List<PaymentMethod> getAllPaymentMethods();

    PaymentMethod getPaymentMethodById(Long id);
}
