package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductSauceEntityMapper {
    ProductSauceEntityMapper INSTANCE = Mappers.getMapper(ProductSauceEntityMapper.class);
}