package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductExtraMapper {

    ProductExtraMapper INSTANCE = Mappers.getMapper(ProductExtraMapper.class);

    @Mapping(target = "price", source = "actualPrice")
    ProductExtra toModel(ProductExtraEntity extras);

}
