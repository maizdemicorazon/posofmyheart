package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.products.catalogs.images.CatalogImageEntity;

@Repository
public interface CatalogImageRepository extends JpaRepository<CatalogImageEntity, Long> {

    /**
     * Busca imágenes por tipo y estado
     */
    @Query("SELECT i FROM CatalogImageEntity i WHERE i.imageType = :imageType AND i.active = :active ORDER BY i.createdAt DESC")
    List<CatalogImageEntity> findByImageTypeAndStatus(
            @Param("imageType") CatalogImageEntity.ImageType imageType,
            @Param("active") boolean active);

    /**
     * Busca imágenes activas por tipo
     */
    @Query("SELECT i FROM CatalogImageEntity i WHERE i.imageType = :imageType AND i.active = true ORDER BY i.createdAt DESC")
    List<CatalogImageEntity> findActiveByImageType(@Param("imageType") CatalogImageEntity.ImageType imageType);

    /**
     * Busca imágenes duplicadas por checksum
     */
    @Query("""
            SELECT i FROM CatalogImageEntity i 
            WHERE i.checksum IN (
                SELECT i2.checksum FROM CatalogImageEntity i2 
                GROUP BY i2.checksum 
                HAVING COUNT(i2.checksum) > 1
            )
            ORDER BY i.checksum, i.createdAt
            """)
    List<CatalogImageEntity> findDuplicatesByChecksum();

    /**
     * Verifica si existe una imagen con el checksum dado
     */
    boolean existsByChecksum(String checksum);

    /**
     * Cuenta imágenes por tipo
     */
    @Query("SELECT COUNT(i) FROM CatalogImageEntity i WHERE i.imageType = :imageType")
    long countByImageType(@Param("imageType") CatalogImageEntity.ImageType imageType);

    /**
     * Busca imágenes activas ordenadas por fecha de creación
     */
    @Query("SELECT i FROM CatalogImageEntity i WHERE i.active = true ORDER BY i.createdAt DESC")
    List<CatalogImageEntity> findAllActive();

    /**
     * Busca imágenes por nombre de archivo
     */
    @Query("SELECT i FROM CatalogImageEntity i WHERE i.fileName LIKE %:fileName% AND i.active = true")
    List<CatalogImageEntity> findByFileNameContaining(@Param("fileName") String fileName);

    /**
     * Obtiene estadísticas de imágenes
     */
    @Query("""
            SELECT i.imageType, i.active, COUNT(i) 
            FROM CatalogImageEntity i 
            GROUP BY i.imageType, i.active 
            ORDER BY i.imageType, i.active
            """)
    List<Object[]> getImageStatistics();
}