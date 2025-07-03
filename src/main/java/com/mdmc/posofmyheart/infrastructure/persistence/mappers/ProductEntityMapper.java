package com.mdmc.posofmyheart.infrastructure.persistence.mappers;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductFlavorEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductVariantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Comparator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Mapper
public interface ProductEntityMapper {
    ProductEntityMapper INSTANCE = Mappers.getMapper(ProductEntityMapper.class);

    default List<ProductEntity> toProductsByIdAsc(List<ProductEntity> products) {
        return products.stream()
                .sorted(Comparator.comparing(ProductEntity::getIdProduct))
                .map(this::toVariantsByIdAsc)
                .map(this::toFlavorsByIdAsc)
                .toList();
    }

    default ProductEntity toVariantsByIdAsc(ProductEntity product) {
        Set<ProductVariantEntity> orderVariants = product.getVariants().stream()
                .sorted(Comparator.comparing(ProductVariantEntity::getIdVariant))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        product.setVariants(orderVariants);
        return product;
    }

    default ProductEntity toFlavorsByIdAsc(ProductEntity product) {
        Set<ProductFlavorEntity> orderFlavors = product.getFlavors().stream()
                .sorted(Comparator.comparing(ProductFlavorEntity::getIdFlavor))
                .collect(Collectors.toCollection(LinkedHashSet::new));

        product.setFlavors(orderFlavors);
        return product;
    }
}
