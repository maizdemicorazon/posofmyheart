package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductExtraEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ProductExtraMapper {

    ProductExtraMapper INSTANCE = Mappers.getMapper(ProductExtraMapper.class);

    @Mapping(target = "price", source = "actualPrice")
    ProductExtra toProductExtra(ProductExtraEntity extra);

    List<ProductExtra> toProductExtras(List<ProductExtraEntity> extras);

}
