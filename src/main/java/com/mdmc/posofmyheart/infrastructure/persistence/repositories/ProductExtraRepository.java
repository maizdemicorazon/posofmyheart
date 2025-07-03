package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.domain.models.ProductExtra;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductExtraEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductExtraRepository extends JpaRepository<ProductExtraEntity, Long> {

    @Query("""
        SELECT new com.mdmc.posofmyheart.domain.models.ProductExtra(
            e.idExtra,
            e.name,
            e.actualPrice
        )
        FROM ProductExtraEntity e
        ORDER BY e.idExtra ASC
        """)
    List<ProductExtra> findAllExtras();
}
