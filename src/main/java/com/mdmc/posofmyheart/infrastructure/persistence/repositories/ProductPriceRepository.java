package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductPriceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

public interface ProductPriceRepository extends JpaRepository<ProductPriceEntity, Long> {
    @Query("SELECT pp FROM ProductPriceEntity pp WHERE pp.product.id = :productId ORDER BY pp.effectiveDate DESC LIMIT 1")
    Optional<ProductPriceEntity> findLatestByProductId(Long productId);
}