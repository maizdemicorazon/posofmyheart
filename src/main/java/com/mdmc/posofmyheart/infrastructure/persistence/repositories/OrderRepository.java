package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * SÚPER OPTIMIZADA: Una query que trae TODAS las relaciones
     * ELIMINA EL PROBLEMA N+1 - LA MEJORA MÁS IMPORTANTE
     */
    @Query("""
        SELECT DISTINCT o FROM OrderEntity o
        LEFT JOIN FETCH o.paymentMethod pm
        LEFT JOIN FETCH o.orderDetails od
        LEFT JOIN FETCH od.product p
        LEFT JOIN FETCH p.category pc
        LEFT JOIN FETCH od.variant v
        LEFT JOIN FETCH od.extraDetails ed
        LEFT JOIN FETCH ed.productExtra pe
        LEFT JOIN FETCH od.sauceDetails sd
        LEFT JOIN FETCH sd.productSauce ps
        LEFT JOIN FETCH od.flavorDetails fd
        LEFT JOIN FETCH fd.flavor f
        ORDER BY o.orderDate DESC
        """)
    List<OrderEntity> findAllWithCompleteDetails();

    /**
     * ⚡ OPTIMIZADA: Busca una orden por ID con todas sus relaciones
     */
    @Query("""
        SELECT DISTINCT o FROM OrderEntity o
        LEFT JOIN FETCH o.paymentMethod pm
        LEFT JOIN FETCH o.orderDetails od
        LEFT JOIN FETCH od.product p
        LEFT JOIN FETCH p.category pc
        LEFT JOIN FETCH od.variant v
        LEFT JOIN FETCH od.extraDetails ed
        LEFT JOIN FETCH ed.productExtra pe
        LEFT JOIN FETCH od.sauceDetails sd
        LEFT JOIN FETCH sd.productSauce ps
        LEFT JOIN FETCH od.flavorDetails fd
        LEFT JOIN FETCH fd.flavor f
        WHERE o.idOrder = :idOrder
        """)
    Optional<OrderEntity> findByIdWithCompleteDetails(@Param("idOrder") Long idOrder);

    /**
     * ⚡ OPTIMIZADA: Consulta por fecha con FETCH JOIN
     */
    @Query("""
        SELECT DISTINCT o FROM OrderEntity o
        LEFT JOIN FETCH o.paymentMethod pm
        LEFT JOIN FETCH o.orderDetails od
        LEFT JOIN FETCH od.product p
        LEFT JOIN FETCH p.category pc
        LEFT JOIN FETCH od.variant v
        LEFT JOIN FETCH od.extraDetails ed
        LEFT JOIN FETCH ed.productExtra pe
        LEFT JOIN FETCH od.sauceDetails sd
        LEFT JOIN FETCH sd.productSauce ps
        LEFT JOIN FETCH od.flavorDetails fd
        LEFT JOIN FETCH fd.flavor f
        WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay
        ORDER BY o.orderDate DESC
        """)
    Set<OrderEntity> findByOrderDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

}