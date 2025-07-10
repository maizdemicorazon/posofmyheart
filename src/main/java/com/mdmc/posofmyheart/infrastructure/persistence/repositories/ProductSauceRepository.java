package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductSauceEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ProductSauceRepository extends JpaRepository<ProductSauceEntity, Long> {

    /**
     * Encuentra todas las salsas activas ordenadas por ID
     */
    @Query("SELECT s FROM ProductSauceEntity s WHERE s.active = true ORDER BY s.idSauce ASC")
    List<ProductSauceEntity> findAllActiveSauces();

    /**
     * Encuentra todas las salsas con sus imágenes
     */
    @Query("SELECT s FROM ProductSauceEntity s LEFT JOIN FETCH s.image ORDER BY s.idSauce ASC")
    List<ProductSauceEntity> findAllWithImages();

    /**
     * Encuentra salsas activas con sus imágenes
     */
    @Query("SELECT s FROM ProductSauceEntity s LEFT JOIN FETCH s.image WHERE s.active = true ORDER BY s.idSauce ASC")
    List<ProductSauceEntity> findActiveWithImages();
}