package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.application.dtos.projection.OrderProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio de reportes de ventas usando solo JPQL estándar
 * Compatible con PostgreSQL
 */
@Repository
public interface SalesReportRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * Obtiene todas las órdenes del período para procesamiento posterior
     * Usa solo JPQL estándar sin funciones específicas de BD
     */
    @Query("""
            SELECT new com.mdmc.posofmyheart.application.dtos.projection.OrderProjection(
                o.orderDate,
                o.totalAmount,
                o.idOrder
            )
            FROM OrderEntity o
            WHERE o.orderDate >= :startDate AND o.orderDate < :endDate
            ORDER BY o.orderDate
            """)
    List<OrderProjection> findAllOrdersInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene ventas por categoría usando JPQL estándar
     */
    @Query("""
            SELECT new com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections$CategorySalesProjection(
                pc.name,
                SUM(od.sellPrice),
                COUNT(od.idOrderDetail)
            )
            FROM OrderEntity o
            JOIN o.orderDetails od
            JOIN od.product p
            JOIN p.category pc
            WHERE o.orderDate >= :startDate AND o.orderDate < :endDate
            GROUP BY pc.idCategory, pc.name
            ORDER BY SUM(od.sellPrice) DESC
            """)
    List<SalesReportProjections.CategorySalesProjection> findCategorySalesInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene resumen del período usando JPQL estándar
     */
    @Query("""
            SELECT new com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections$PeriodSummaryProjection(
                SUM(o.totalAmount),
                COUNT(o.idOrder)
            )
            FROM OrderEntity o
            WHERE o.orderDate >= :startDate AND o.orderDate < :endDate
            """)
    SalesReportProjections.PeriodSummaryProjection findPeriodSummary(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}