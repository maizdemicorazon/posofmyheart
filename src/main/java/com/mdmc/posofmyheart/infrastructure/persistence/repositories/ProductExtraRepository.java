package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductExtraRepository extends JpaRepository<ProductExtraEntity, Long> {
}
