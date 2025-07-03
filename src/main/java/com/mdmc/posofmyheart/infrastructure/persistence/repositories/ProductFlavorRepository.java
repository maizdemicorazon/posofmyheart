package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductFlavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductFlavorRepository extends JpaRepository<ProductFlavorEntity, Long> {

    /**
     * Sabores activos por producto con relaciones
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId AND pf.active = true ORDER BY pf.name ASC")
    List<ProductFlavorEntity> findActiveFlavorsByProductIdWithProduct(@Param("productId") Long productId);

    /**
     * Todos los sabores por producto con relaciones
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId ORDER BY pf.name ASC")
    List<ProductFlavorEntity> findAllFlavorsByProductIdWithProduct(@Param("productId") Long productId);

    /**
     * Solo verificar existencia
     */
    @Query("SELECT COUNT(pf) > 0 FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId AND pf.idFlavor = :flavorId")
    boolean existsByProductIdAndFlavorId(@Param("productId") Long productId, @Param("flavorId") Long flavorId);

}