package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {
}
