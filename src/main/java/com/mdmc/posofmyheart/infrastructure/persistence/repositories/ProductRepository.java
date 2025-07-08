package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.ProductEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.custom.ProductRepositoryCustom;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long>, ProductRepositoryCustom {
    /**
     * Busca productos con todas sus relaciones
     */
    @EntityGraph(value = "Product.withAllRelations", type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT p FROM ProductEntity p ORDER BY p.idProduct ASC")
    List<ProductEntity> findByIdWithAllRelations();

    @Query("SELECT p.image FROM ProductEntity p WHERE p.idProduct = :idProduct")
    Optional<byte[]> getImageById(@Param("idProduct") Long idProduct);

}