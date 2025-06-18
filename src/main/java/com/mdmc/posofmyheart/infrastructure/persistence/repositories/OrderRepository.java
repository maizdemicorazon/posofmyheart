package com.mdmc.posofmyheart.infrastructure.persistence.repositories;

import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    @Query("SELECT o FROM OrderEntity o WHERE o.orderDate BETWEEN :start AND :end")
    List<OrderEntity> findByDateRange(@Param("start") LocalDateTime start,
                                      @Param("end") LocalDateTime end);

    @Query("SELECT o FROM OrderEntity o WHERE DATE(o.orderDate) = :date ORDER BY o.orderDate DESC")
    List<OrderEntity> findByOrderDate(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product",
            "orderDetails.variant",
            "orderDetails.extraDetails",
            "orderDetails.extraDetails.productExtra",
            "orderDetails.sauceDetails",
            "orderDetails.sauceDetails.sauce",
            "orderDetails.flavorDetails",
            "orderDetails.flavorDetails.flavor"
    })
    @Query("SELECT o FROM OrderEntity o")
    List<OrderEntity> findAllWithDetails();

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product",
            "orderDetails.variant",
            "orderDetails.extraDetails",
            "orderDetails.extraDetails.productExtra",
            "orderDetails.sauceDetails",
            "orderDetails.sauceDetails.sauce",
            "orderDetails.flavorDetails",
            "orderDetails.flavorDetails.flavor"
    })
    @Query("SELECT o FROM OrderEntity o WHERE o.idOrder = :id")
    OrderEntity findByIdWithDetails(@Param("id") Long id);

    @EntityGraph(attributePaths = {
            "orderDetails",
            "orderDetails.product",
            "orderDetails.variant",
            "orderDetails.extraDetails",
            "orderDetails.extraDetails.productExtra",
            "orderDetails.sauceDetails",
            "orderDetails.sauceDetails.sauce",
            "orderDetails.flavorDetails",
            "orderDetails.flavorDetails.flavor"
    })
    @Query("SELECT o FROM OrderEntity o WHERE DATE(o.orderDate) = :date ORDER BY o.orderDate DESC")
    List<OrderEntity> findByOrderDateWithDetails(@Param("date") LocalDate date);

}