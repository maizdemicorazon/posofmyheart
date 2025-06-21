package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.domain.models.ProductSauce;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductSauceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductSauceRepository extends JpaRepository<ProductSauceEntity, Long> {
    @Query("""
            SELECT new com.mdmc.posofmyheart.domain.models.ProductSauce(
                s.idSauce,
                s.name,
                s.image
            )
            FROM ProductSauceEntity s
             ORDER BY s.idSauce ASC
            """)
    List<ProductSauce> findAllSauces();
}
