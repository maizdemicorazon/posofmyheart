package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.api.exceptions.PayMethodNotFoundException;
import com.mdmc.posofmyheart.application.services.PaymentMethodService;
import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.mappers.PaymentMethodEntityMapper;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.PaymentMethodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    @Override
    public List<PaymentMethod> getAllPaymentMethods() {
        return PaymentMethodEntityMapper.INSTANCE.toPaymentMethodList(
                paymentMethodRepository.findAll()
        );
    }

    @Override
    public PaymentMethod getPaymentMethodById(Long id) {
        return PaymentMethodEntityMapper.INSTANCE.toPaymentMethod(
                paymentMethodRepository.findById(id)
                        .orElseThrow(() -> new PayMethodNotFoundException(id))
        );
    }

}
