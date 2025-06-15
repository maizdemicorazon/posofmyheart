package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductFlavor;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductFlavorMapper {

    ProductFlavorMapper INSTANCE = Mappers.getMapper(ProductFlavorMapper.class);

    ProductFlavor toProductFlavor(ProductFlavorEntity flavorEntity);
    List<ProductFlavor> toProductFlavors(List<ProductFlavorEntity> flavorEntities);

}
