package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductVariantEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.SauceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VariantRepository extends JpaRepository<ProductVariantEntity, Integer> {
}
