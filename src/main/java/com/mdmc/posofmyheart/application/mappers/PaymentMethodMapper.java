package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface PaymentMethodMapper {
    PaymentMethodMapper INSTANCE = Mappers.getMapper(PaymentMethodMapper.class);

    PaymentMethod toDomain(PaymentMethodEntity entity);

    List<PaymentMethod> toDomainList(List<PaymentMethodEntity> entities);

}