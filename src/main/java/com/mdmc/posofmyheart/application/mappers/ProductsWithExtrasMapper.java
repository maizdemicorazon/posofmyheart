package com.mdmc.posofmyheart.application.mappers;

import com.mdmc.posofmyheart.domain.dtos.ProductsWithExtrasDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.PaymentMethodEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(uses = {ProductMapper.class, ProductExtraMapper.class, SauceMapper.class, PaymentMethodsMapper.class})
public interface ProductsWithExtrasMapper {
    ProductsWithExtrasMapper INSTANCE = Mappers.getMapper(ProductsWithExtrasMapper.class);

    ProductsWithExtrasDto toDtoFromEntities(List<ProductEntity> products,
                                            List<ProductExtraEntity> extras,
                                            List<ProductSauceEntity> sauces,
                                            List<PaymentMethodEntity> paymentMethods);
}