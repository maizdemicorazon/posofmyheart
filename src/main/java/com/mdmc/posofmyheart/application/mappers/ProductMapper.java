package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.models.Product;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(uses = {ProductVariantMapper.class, ProductFlavorMapper.class})
public interface ProductMapper {

    ProductMapper INSTANCE = Mappers.getMapper(ProductMapper.class);

    @Mapping(target = "idCategory", source = "category.idCategory")
    @Mapping(target = "options", source = "variants")
    @Mapping(target = "flavors", source = "flavors")
    Product toProduct(ProductEntity entity);


}
