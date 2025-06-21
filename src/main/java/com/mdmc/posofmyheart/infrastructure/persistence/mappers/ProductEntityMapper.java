package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductEntityMapper {
    ProductEntityMapper INSTANCE = Mappers.getMapper(ProductEntityMapper.class);
}
