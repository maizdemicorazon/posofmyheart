package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ProductFlavorMapper {

    ProductFlavorMapper INSTANCE = Mappers.getMapper(ProductFlavorMapper.class);

    ProductFlavor toModel(ProductFlavorEntity flavorEntity);

}
