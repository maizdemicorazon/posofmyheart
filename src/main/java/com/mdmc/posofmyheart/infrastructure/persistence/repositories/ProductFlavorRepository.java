package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.ProductFlavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductFlavorRepository extends JpaRepository<ProductFlavorEntity, Long> {
    @Query("SELECT pf FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId AND pf.active = true")
    List<ProductFlavorEntity> findActiveFlavorsByProductId(@Param("productId") Long productId);

    @Query("SELECT pf FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId")
    List<ProductFlavorEntity> findAllFlavorsByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(pf) > 0 FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId AND pf.idFlavor = :flavorId")
    boolean existsByProductIdAndFlavorId(@Param("productId") Long productId, @Param("flavorId") Long flavorId);
}
