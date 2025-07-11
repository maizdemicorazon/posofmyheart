package com.mdmc.posofmyheart.application.services;

import java.util.List;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;

public interface PaymentMethodService {

    List<PaymentMethod> getAllPaymentMethods();

    PaymentMethod getPaymentMethodById(Long id);
}
