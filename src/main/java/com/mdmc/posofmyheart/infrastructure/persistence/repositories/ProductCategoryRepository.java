package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductCategoryRepository extends JpaRepository<ProductCategoryEntity, Long> {
}
