package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductSauceMapper {
    ProductSauceMapper INSTANCE = Mappers.getMapper(ProductSauceMapper.class);

    ProductSauce toProductSauce(ProductSauceEntity sauceEntity);

    List<ProductSauce> toProductSauces(List<ProductSauceEntity> sauceEntities);

}
