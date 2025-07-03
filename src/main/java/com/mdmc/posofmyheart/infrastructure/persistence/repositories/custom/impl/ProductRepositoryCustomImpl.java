package com.mdmc.posofmyheart.infrastructure.persistence.repositories.custom.impl;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductCategoryEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.custom.ProductRepositoryCustom;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.*;

import java.util.List;

public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Object[]> getAvailableProducts() {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Object[]> query = cb.createQuery(Object[].class);

        Root<ProductEntity> product = query.from(ProductEntity.class);
        Join<ProductEntity, ProductVariantEntity> variant = product.join("variants", JoinType.INNER);
        Join<ProductEntity, ProductCategoryEntity> category = product.join("category", JoinType.INNER);

        query.multiselect(
                product.get("name"),
                variant.get("size"),
                variant.get("actualSellPrice"),
                category.get("name"),
                product.get("description")
        );

        query.where(cb.lessThanOrEqualTo(variant.get("effectiveDate"), cb.currentDate()));

        query.orderBy(
                cb.asc(category.get("name")),
                cb.asc(product.get("name")),
                cb.asc(variant.get("actualSellPrice"))
        );

        return entityManager.createQuery(query).getResultList();
    }
}
