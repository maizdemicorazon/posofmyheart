package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductExtrasEntityMapper {
    ProductExtrasEntityMapper INSTANCE = Mappers.getMapper(ProductExtrasEntityMapper.class);
}
