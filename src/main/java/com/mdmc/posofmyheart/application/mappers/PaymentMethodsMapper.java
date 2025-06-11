package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMethodsMapper {
    PaymentMethodsMapper INSTANCE = Mappers.getMapper(PaymentMethodsMapper.class);

    PaymentMethod toPaymentMethod(PaymentMethodEntity entity);
}
