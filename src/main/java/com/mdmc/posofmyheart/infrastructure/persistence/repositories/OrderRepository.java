package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("""
        SELECT o FROM OrderEntity o
        WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay
        ORDER BY o.orderDate DESC
        """)
    List<OrderEntity> findByOrderDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * Encuentra órdenes por rango de fechas (para análisis detallado)
     */
    @Query("""
        SELECT o FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate 
        ORDER BY o.orderDate DESC
        """)
    List<OrderEntity> findOrdersByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}