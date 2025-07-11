package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductCategoryEntity;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
}
