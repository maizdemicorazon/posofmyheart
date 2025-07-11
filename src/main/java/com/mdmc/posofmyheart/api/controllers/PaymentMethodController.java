package com.mdmc.posofmyheart.api.controllers;

import java.util.List;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mdmc.posofmyheart.application.services.PaymentMethodService;
import com.mdmc.posofmyheart.domain.models.PaymentMethod;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    @Cacheable("methods")
    @GetMapping
    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodService.getAllPaymentMethods();
    }

    @GetMapping("/{idPay}")
    public PaymentMethod getPaymentMethodById(@PathVariable Long idPay) {
        return paymentMethodService.getPaymentMethodById(idPay);
    }

}