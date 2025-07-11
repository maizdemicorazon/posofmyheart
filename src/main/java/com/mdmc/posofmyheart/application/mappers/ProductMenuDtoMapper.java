package com.mdmc.posofmyheart.application.mappers;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.mdmc.posofmyheart.domain.dtos.ProductsMenuDto;
import com.mdmc.posofmyheart.domain.models.PaymentMethod;
import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;

@Mapper(uses = {ProductMapper.class, ProductExtraMapper.class, PaymentMethodMapper.class})
public interface ProductMenuDtoMapper {
    ProductMenuDtoMapper INSTANCE = Mappers.getMapper(ProductMenuDtoMapper.class);

    ProductsMenuDto toProductsMenu(List<ProductEntity> products,
                                   List<ProductExtra> extras,
                                   List<ProductSauce> sauces,
                                   List<PaymentMethod> paymentMethods);
}