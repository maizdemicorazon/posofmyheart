package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductExtraEntity;

public interface ProductExtraRepository extends JpaRepository<ProductExtraEntity, Long> {

    /**
     * Encuentra todos los extras activos con sus imágenes ordenados por ID
     */
    @Query("SELECT e FROM ProductExtraEntity e LEFT JOIN FETCH e.image WHERE e.active = true ORDER BY e.idExtra ASC")
    List<ProductExtraEntity> findAllActiveWithImages();

    /**
     * Encuentra todos los extras con sus imágenes ordenados por ID
     */
    @Query("SELECT e FROM ProductExtraEntity e LEFT JOIN FETCH e.image ORDER BY e.idExtra ASC")
    List<ProductExtraEntity> findAllWithImages();

    /**
     * Encuentra extras activos básicos sin imágenes
     */
    @Query("SELECT e FROM ProductExtraEntity e WHERE e.active = true ORDER BY e.idExtra ASC")
    List<ProductExtraEntity> findAllActive();
}