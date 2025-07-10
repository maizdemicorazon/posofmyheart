package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.ProductVariantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductVariantRepository extends JpaRepository<ProductVariantEntity, Long> {

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId ORDER BY pv.actualSellPrice ASC")
    List<ProductVariantEntity> findVariantsByProductId(@Param("productId") Long productId);

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId AND pv.actualSellPrice IS NOT NULL ORDER BY pv.actualSellPrice ASC")
    List<ProductVariantEntity> findActiveVariantsByProductId(@Param("productId") Long productId);

    @Query("SELECT COUNT(pv) > 0 FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId AND pv.idVariant = :variantId")
    boolean existsByProductIdAndVariantId(@Param("productId") Long productId, @Param("variantId") Long variantId);

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId AND pv.actualSellPrice >= :minPrice AND pv.actualSellPrice <= :maxPrice ORDER BY pv.actualSellPrice ASC")
    List<ProductVariantEntity> findVariantsByProductIdAndPriceRange(
            @Param("productId") Long productId,
            @Param("minPrice") BigDecimal minPrice,
            @Param("maxPrice") BigDecimal maxPrice
    );

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId ORDER BY pv.actualSellPrice ASC LIMIT 1")
    Optional<ProductVariantEntity> findCheapestVariantByProductId(@Param("productId") Long productId);

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId ORDER BY pv.actualSellPrice DESC LIMIT 1")
    Optional<ProductVariantEntity> findMostExpensiveVariantByProductId(@Param("productId") Long productId);

    @Query("SELECT pv FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId AND pv.size = :size")
    Optional<ProductVariantEntity> findByProductIdAndSize(@Param("productId") Long productId, @Param("size") String size);

    @Query("SELECT DISTINCT pv.size FROM ProductVariantEntity pv WHERE pv.product.idProduct = :productId ORDER BY pv.size")
    List<String> findDistinctSizesByProductId(@Param("productId") Long productId);
}