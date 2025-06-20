package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * ⚡ SÚPER OPTIMIZADA: Busca todas las órdenes con todas sus relaciones
     * Usa EntityGraph para evitar el problema N+1 de forma declarativa
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o ORDER BY o.orderDate DESC")
    List<OrderEntity> findAllWithCompleteDetails();

    /**
     * ⚡ OPTIMIZADA: Busca una orden por ID con todas sus relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT o FROM OrderEntity o WHERE o.idOrder = :idOrder")
    Optional<OrderEntity> findByIdWithCompleteDetails(@Param("idOrder") Long idOrder);

    /**
     * ⚡ OPTIMIZADA: Consulta por fecha con EntityGraph
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay ORDER BY o.orderDate DESC")
    Set<OrderEntity> findByOrderDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * ⚡ BÁSICA: Solo carga orden con payment method (para casos simples)
     */
    @EntityGraph(value = "Order.basic", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT o FROM OrderEntity o WHERE o.idOrder = :idOrder")
    Optional<OrderEntity> findByIdBasic(@Param("idOrder") Long idOrder);

    /**
     * ⚡ INTERMEDIA: Carga orden con detalles básicos (sin extras, salsas, sabores)
     */
    @EntityGraph(value = "Order.withOrderDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o ORDER BY o.orderDate DESC")
    List<OrderEntity> findAllWithOrderDetails();

    /**
     * ⚡ BÚSQUEDA POR CLIENTE: Con todas las relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE LOWER(o.clientName) LIKE LOWER(CONCAT('%', :clientName, '%')) ORDER BY o.orderDate DESC")
    List<OrderEntity> findByClientNameContainingIgnoreCase(@Param("clientName") String clientName);

    /**
     * ⚡ BÚSQUEDA POR RANGO DE TOTALES: Con todas las relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE o.totalAmount BETWEEN :minAmount AND :maxAmount ORDER BY o.orderDate DESC")
    List<OrderEntity> findByTotalAmountBetween(
            @Param("minAmount") java.math.BigDecimal minAmount,
            @Param("maxAmount") java.math.BigDecimal maxAmount
    );

    /**
     * ⚡ BÚSQUEDA POR MÉTODO DE PAGO: Con todas las relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE o.paymentMethod.idPayment = :paymentMethodId ORDER BY o.orderDate DESC")
    List<OrderEntity> findByPaymentMethodId(@Param("paymentMethodId") Long paymentMethodId);

    /**
     * ⚡ ÓRDENES RECIENTES: Últimas N órdenes con todas las relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o ORDER BY o.orderDate DESC LIMIT :limit")
    List<OrderEntity> findRecentOrders(@Param("limit") int limit);

    /**
     * ⚡ CONTEO OPTIMIZADO: Sin cargar relaciones (para dashboard)
     */
    @Query("SELECT COUNT(o) FROM OrderEntity o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay")
    Long countOrdersByDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * ⚡ SUMA DE TOTALES: Sin cargar relaciones (para dashboard)
     */
    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM OrderEntity o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay")
    java.math.BigDecimal sumTotalAmountByDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * ⚡ EXISTE ORDEN: Verificación sin cargar la entidad
     */
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderEntity o WHERE o.idOrder = :idOrder")
    boolean existsByIdOrder(@Param("idOrder") Long idOrder);
}