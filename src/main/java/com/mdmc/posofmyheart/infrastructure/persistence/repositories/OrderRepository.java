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
     * Encuentra datos de ventas por rango de fechas
     * Retorna: [fecha, total_ventas, cantidad_ordenes]
     */
    @Query("""
        SELECT 
            DATE(o.orderDate) as fecha,
            COALESCE(SUM(o.totalAmount), 0) as total_ventas,
            COUNT(o.idOrder) as cantidad_ordenes
        FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate 
        GROUP BY DATE(o.orderDate)
        ORDER BY DATE(o.orderDate)
        """)
    List<Object[]> findSalesDataByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra datos de ganancias por rango de fechas
     * Retorna: [fecha, cantidad_ordenes, total_ventas]
     */
    @Query("""
        SELECT 
            DATE(o.orderDate) as fecha,
            COUNT(o.idOrder) as cantidad_ordenes,
            COALESCE(SUM(o.totalAmount), 0) as total_ventas
        FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate 
        GROUP BY DATE(o.orderDate)
        ORDER BY DATE(o.orderDate)
        """)
    List<Object[]> findEarningsDataByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra ventas por categoría en un rango de fechas
     * Retorna: [nombre_categoria, total_ventas, cantidad_ordenes]
     */
    @Query("""
        SELECT 
            pc.name as categoria,
            COALESCE(SUM(od.sellPrice), 0) as total_ventas,
            COUNT(DISTINCT o.idOrder) as cantidad_ordenes
        FROM OrderEntity o
        JOIN o.orderDetails od
        JOIN od.product p
        JOIN p.category pc
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        GROUP BY pc.name
        ORDER BY SUM(od.sellPrice) DESC
        """)
    List<Object[]> findSalesByCategory(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
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


    /**
     * Calcula el total de ventas para un período
     */
    @Query("""
        SELECT COALESCE(SUM(o.totalAmount), 0) 
        FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        """)
    Double calculateTotalSales(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Cuenta órdenes para un período
     */
    @Query("""
        SELECT COUNT(o.idOrder) 
        FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        """)
    Long countOrdersInPeriod(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra productos más vendidos
     */
    @Query("""
        SELECT 
            p.name as producto,
            COUNT(od) as cantidad_vendida,
            COALESCE(SUM(od.sellPrice), 0) as total_ventas
        FROM OrderEntity o
        JOIN o.orderDetails od
        JOIN od.product p
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        GROUP BY p.idProduct, p.name
        ORDER BY SUM(od.sellPrice) DESC
        """)
    List<Object[]> findTopSellingProducts(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra el ticket promedio por período
     */
    @Query("""
        SELECT AVG(o.totalAmount) 
        FROM OrderEntity o 
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        """)
    Double findAverageTicket(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Query para obtener métodos de pago más usados
     */
    @Query("""
        SELECT 
            pm.name as metodo_pago,
            COUNT(o) as cantidad_ordenes,
            COALESCE(SUM(o.totalAmount), 0) as total_ventas
        FROM OrderEntity o
        JOIN o.paymentMethod pm
        WHERE o.orderDate BETWEEN :startDate AND :endDate
        GROUP BY pm.idPayment, pm.name
        ORDER BY COUNT(o) DESC
        """)
    List<Object[]> findPaymentMethodStats(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

}