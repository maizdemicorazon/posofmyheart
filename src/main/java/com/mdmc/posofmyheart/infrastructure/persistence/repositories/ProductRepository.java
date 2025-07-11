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

    /**
     * Busca productos activos con solo imagen principal
     */
    @Query("""
            SELECT p FROM ProductEntity p 
            LEFT JOIN FETCH p.image 
            WHERE p.active = true 
            ORDER BY p.idProduct ASC
            """)
    List<ProductEntity> findActiveWithImages();

    /**
     * Busca productos por categoría con imágenes
     */
    @Query("""
            SELECT p FROM ProductEntity p 
            LEFT JOIN FETCH p.image 
            LEFT JOIN FETCH p.category 
            WHERE p.category.idCategory = :categoryId AND p.active = true 
            ORDER BY p.name ASC
            """)
    List<ProductEntity> findByCategoryWithImages(@Param("categoryId") Long categoryId);

    /**
     * Busca productos que tengan sabores disponibles
     */
    @Query("""
            SELECT DISTINCT p FROM ProductEntity p 
            LEFT JOIN FETCH p.image 
            LEFT JOIN FETCH p.flavors f 
            LEFT JOIN FETCH f.image 
            WHERE p.active = true AND SIZE(p.flavors) > 0 
            ORDER BY p.idProduct ASC
            """)
    List<ProductEntity> findActiveWithFlavorsAndImages();

    /**
     * Obtiene solo los datos de imagen de un producto
     */
    @Query("SELECT p.image.imageData FROM ProductEntity p WHERE p.idProduct = :idProduct AND p.image IS NOT NULL")
    Optional<byte[]> getImageById(@Param("idProduct") Long idProduct);

    /**
     * Verifica si un producto tiene imagen
     */
    @Query("SELECT CASE WHEN p.image IS NOT NULL THEN true ELSE false END FROM ProductEntity p WHERE p.idProduct = :idProduct")
    boolean hasImage(@Param("idProduct") Long idProduct);

    /**
     * Cuenta productos por categoría que tienen imagen
     */
    @Query("SELECT COUNT(p) FROM ProductEntity p WHERE p.category.idCategory = :categoryId AND p.image IS NOT NULL AND p.active = true")
    long countByCategoryWithImage(@Param("categoryId") Long categoryId);
}