package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.PaymentMethodEntity;

@Mapper
public interface PaymentMethodEntityMapper {
    PaymentMethodEntityMapper INSTANCE = Mappers.getMapper(PaymentMethodEntityMapper.class);

    PaymentMethod toPaymentMethod(PaymentMethodEntity entity);

    List<PaymentMethod> toPaymentMethodList(List<PaymentMethodEntity> entities);

}