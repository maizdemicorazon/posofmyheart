package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.custom.ProductRepositoryCustom;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {

    /**
     * Busca productos con todas sus relaciones incluyendo imágenes
     */
    @EntityGraph(value = "Product.withAllRelations", type = EntityGraph.EntityGraphType.LOAD)
    @Query("""
            SELECT DISTINCT p FROM ProductEntity p 
            LEFT JOIN FETCH p.category 
            LEFT JOIN FETCH p.variants 
            LEFT JOIN FETCH p.flavors f 
            WHERE p.active = true 
            ORDER BY p.idProduct ASC
            """)
    List<ProductEntity> findByIdWithAllRelations();

    /**
     * Busca un producto por ID con todas sus relaciones e imágenes
     */
    @Query("""
            SELECT p FROM ProductEntity p 
            LEFT JOIN FETCH p.image 
            LEFT JOIN FETCH p.category 
            LEFT JOIN FETCH p.variants 
            LEFT JOIN FETCH p.flavors f 
            LEFT JOIN FETCH f.image 
            WHERE p.idProduct = :productId
            """)
    Optional<ProductEntity> findByIdWithAllRelationsAndImages(@Param("productId") Long productId);

}