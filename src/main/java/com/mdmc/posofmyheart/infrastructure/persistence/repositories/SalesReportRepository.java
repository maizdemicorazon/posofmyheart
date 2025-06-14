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

    /**
     * Obtiene datos detallados de órdenes con categorías para análisis completo
     */
    @Query("""
        SELECT new com.mdmc.posofmyheart.application.dtos.projection.OrderProjection(
            o.orderDate,
            o.totalAmount,
            o.idOrder,
            pc.name,
            od.sellPrice
        )
        FROM OrderEntity o
        JOIN o.orderDetails od
        JOIN od.product p
        JOIN p.category pc
        WHERE o.orderDate >= :startDate AND o.orderDate < :endDate
        ORDER BY o.orderDate
        """)
    List<OrderProjection> findDetailedOrdersInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene órdenes con información completa incluyendo cliente y método de pago
     */
    @Query("""
        SELECT new com.mdmc.posofmyheart.application.dtos.projection.OrderProjection(
            o.orderDate,
            o.totalAmount,
            o.idOrder,
            pc.name,
            od.sellPrice,
            o.clientName,
            pm.name
        )
        FROM OrderEntity o
        JOIN o.orderDetails od
        JOIN od.product p
        JOIN p.category pc
        JOIN o.paymentMethod pm
        WHERE o.orderDate >= :startDate AND o.orderDate < :endDate
        ORDER BY o.orderDate
        """)
    List<OrderProjection> findCompleteOrdersInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene ventas diarias usando query nativa para PostgreSQL
     * Nota: Esta query es específica para PostgreSQL
     */
    @Query(value = """
        SELECT 
            o.order_date as orderDate,
            SUM(o.total_amount) as totalSales,
            COUNT(o.id_order) as totalOrders
        FROM orders o
        WHERE o.order_date >= :startDate AND o.order_date < :endDate
        GROUP BY DATE(o.order_date), o.order_date
        ORDER BY DATE(o.order_date)
        """, nativeQuery = true)
    List<Object[]> findDailySalesGroupedNative(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);

    /**
     * Obtiene horarios pico usando query nativa para PostgreSQL
     */
    @Query(value = """
        SELECT 
            EXTRACT(HOUR FROM o.order_date) as hour,
            SUM(o.total_amount) as totalSales,
            COUNT(o.id_order) as totalOrders
        FROM orders o
        WHERE o.order_date >= :startDate AND o.order_date < :endDate
        GROUP BY EXTRACT(HOUR FROM o.order_date)
        ORDER BY SUM(o.total_amount) DESC
        LIMIT 5
        """, nativeQuery = true)
    List<Object[]> findPeakHoursNative(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate);
}