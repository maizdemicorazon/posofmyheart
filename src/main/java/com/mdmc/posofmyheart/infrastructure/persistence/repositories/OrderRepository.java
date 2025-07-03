package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.domain.OrderStatusEnum;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.Modifying;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * Busca todas las órdenes con todas sus relaciones
     * Usa EntityGraph para evitar el problema N+1 de forma declarativa
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o ORDER BY o.idOrder DESC")
    List<OrderEntity> findAllWithDetails();

    /**
     * Busca una orden por ID con todas sus relaciones
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT o FROM OrderEntity o WHERE o.idOrder = :idOrder")
    Optional<OrderEntity> findByIdWithDetails(@Param("idOrder") Long idOrder);

    /**
     * Consulta por fecha con EntityGraph
     */
    @EntityGraph(value = "Order.withCompleteDetails", type = EntityGraph.EntityGraphType.FETCH)
    @Query("SELECT DISTINCT o FROM OrderEntity o WHERE o.orderDate >= :startOfDay AND o.orderDate < :endOfDay ORDER BY o.orderDate DESC")
    Set<OrderEntity> findByOrderDate(
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay
    );

    /**
     * EXISTE ORDEN: Verificación sin cargar la entidad
     */
    @Query("SELECT CASE WHEN COUNT(o) > 0 THEN true ELSE false END FROM OrderEntity o WHERE o.idOrder = :idOrder")
    boolean existsByIdOrder(@Param("idOrder") Long idOrder);

    /**
     * Actualizar campo status de la orden
     */
    @Modifying
    @Query("UPDATE OrderEntity o SET o.status = :status WHERE o.idOrder = :idOrder")
    void updateOrderStatus(@Param("idOrder") Long idOrder, @Param("status") OrderStatusEnum status);
}