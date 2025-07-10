package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);

    PaymentMethod toPaymentMethod(PaymentMethodEntity entity);

}