package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE DATE(o.orderDate) = :date ORDER BY o.orderDate DESC")
    List<OrderEntity> findByOrderDate(@Param("date") LocalDate date);

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
     * Cuenta órdenes por día de la semana
     */
    @Query(value = """
        SELECT 
            EXTRACT(DOW FROM o.order_date) as dia_semana,
            COUNT(o.id_order) as cantidad,
            COALESCE(SUM(o.total_amount), 0) as total_ventas
        FROM orders o 
        WHERE o.order_date BETWEEN :startDate AND :endDate 
        GROUP BY EXTRACT(DOW FROM o.order_date)
        ORDER BY dia_semana
        """, nativeQuery = true)
    List<Object[]> findOrdersByDayOfWeek(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra ventas por hora del día
     */
    @Query(value = """
        SELECT 
            EXTRACT(HOUR FROM o.order_date) as hora,
            COUNT(o.id_order) as cantidad,
            COALESCE(SUM(o.total_amount), 0) as total_ventas
        FROM orders o 
        WHERE o.order_date BETWEEN :startDate AND :endDate 
        GROUP BY EXTRACT(HOUR FROM o.order_date)
        ORDER BY hora
        """, nativeQuery = true)
    List<Object[]> findOrdersByHour(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Encuentra órdenes del día actual
     */
    @Query(value = """
        SELECT o.* FROM orders o 
        WHERE DATE(o.order_date) = CURRENT_DATE
        ORDER BY o.order_date DESC
        """, nativeQuery = true)
    List<OrderEntity> findTodayOrders();

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
     * Query nativa para obtener órdenes con detalles por rango de fechas
     */
    @Query(value = """
        SELECT DISTINCT o.* 
        FROM orders o 
        WHERE o.order_date BETWEEN :startDate AND :endDate 
        ORDER BY o.order_date DESC
        """, nativeQuery = true)
    List<OrderEntity> findOrdersWithDetailsByDateRange(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );

    /**
     * Query nativa para productos más vendidos con detalles completos
     */
    @Query(value = """
        SELECT 
            p.id_product,
            p.name as producto,
            COUNT(od.id_order_detail) as cantidad_vendida,
            COALESCE(SUM(od.quantity), 0) as unidades_vendidas,
            COALESCE(SUM(od.sell_price), 0) as total_ventas,
            COALESCE(AVG(od.sell_price), 0) as precio_promedio
        FROM orders o
        INNER JOIN order_details od ON od.id_order = o.id_order
        INNER JOIN products p ON p.id_product = od.id_product
        WHERE o.order_date BETWEEN :startDate AND :endDate
        GROUP BY p.id_product, p.name
        ORDER BY SUM(od.sell_price) DESC
        LIMIT 10
        """, nativeQuery = true)
    List<Object[]> findTopSellingProductsDetailed(
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

    /**
     * Query para análisis de extras más vendidos
     */
    @Query(value = """
        SELECT 
            e.name as extra,
            COUNT(ode.id_order_detail_extra) as cantidad_vendida,
            COALESCE(SUM(ode.quantity), 0) as unidades_totales,
            COALESCE(SUM(ode.quantity * e.price), 0) as total_ventas
        FROM orders o
        INNER JOIN order_details od ON od.id_order = o.id_order
        INNER JOIN order_detail_extras ode ON ode.id_order_detail = od.id_order_detail
        INNER JOIN extras e ON e.id_extra = ode.id_extra
        WHERE o.order_date BETWEEN :startDate AND :endDate
        GROUP BY e.id_extra, e.name
        ORDER BY SUM(ode.quantity * e.price) DESC
        """, nativeQuery = true)
    List<Object[]> findTopSellingExtras(
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
}