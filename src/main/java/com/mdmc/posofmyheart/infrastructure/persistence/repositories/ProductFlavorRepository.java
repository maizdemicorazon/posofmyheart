package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductFlavorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductFlavorRepository extends JpaRepository<ProductFlavorEntity, Long> {

    /**
     * Sabores activos por producto con relaciones e imágenes
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf LEFT JOIN FETCH pf.image LEFT JOIN FETCH pf.product WHERE pf.product.idProduct = :productId AND pf.active = true ORDER BY pf.name ASC")
    List<ProductFlavorEntity> findActiveFlavorsByProductIdWithProduct(@Param("productId") Long productId);

    /**
     * Todos los sabores por producto con relaciones e imágenes
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf LEFT JOIN FETCH pf.image LEFT JOIN FETCH pf.product WHERE pf.product.idProduct = :productId ORDER BY pf.name ASC")
    List<ProductFlavorEntity> findAllFlavorsByProductIdWithProduct(@Param("productId") Long productId);

    /**
     * Solo verificar existencia
     */
    @Query("SELECT COUNT(pf) > 0 FROM ProductFlavorEntity pf WHERE pf.product.idProduct = :productId AND pf.idFlavor = :flavorId")
    boolean existsByProductIdAndFlavorId(@Param("productId") Long productId, @Param("flavorId") Long flavorId);

    /**
     * Encuentra todos los sabores con sus imágenes
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf LEFT JOIN FETCH pf.image ORDER BY pf.idFlavor ASC")
    List<ProductFlavorEntity> findAllWithImages();

    /**
     * Encuentra sabores activos con sus imágenes
     */
    @Query("SELECT pf FROM ProductFlavorEntity pf LEFT JOIN FETCH pf.image WHERE pf.active = true ORDER BY pf.idFlavor ASC")
    List<ProductFlavorEntity> findActiveWithImages();
}