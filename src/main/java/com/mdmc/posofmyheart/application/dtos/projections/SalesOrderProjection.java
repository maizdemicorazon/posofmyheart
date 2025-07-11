package com.mdmc.posofmyheart.application.dtos.projections;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Proyección para órdenes usada en reportes de ventas
 * Clase concreta para mantener independencia de base de datos
 * Compatible con constructores JPQL new
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesOrderProjection {

    /**
     * Fecha y hora de la orden
     */
    private LocalDateTime createdAt;

    /**
     * Monto total de la orden
     */
    private BigDecimal totalAmount;

    /**
     * ID de la orden
     */
    private Long orderId;

    /**
     * Nombre de la categoría del producto (opcional)
     * Usado para análisis por categorías
     */
    private String categoryName;

    /**
     * Precio de venta del detalle de orden (opcional)
     * Usado para análisis detallado de productos
     */
    private BigDecimal orderDetailSellPrice;

    /**
     * Nombre del cliente (opcional)
     * Útil para análisis de clientes
     */
    private String clientName;

    /**
     * Campo de forma de pago (opcional)
     * Útil para análisis de métodos de pago
     */
    private String paymentMethodName;

    /**
     * Constructor para consultas básicas de órdenes
     * Usado en findAllOrdersInPeriod
     */
    public SalesOrderProjection(LocalDateTime createdAt, BigDecimal totalAmount, Long orderId) {
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
    }

    /**
     * Constructor para consultas con categoría
     * Usado en análisis por categorías
     */
    public SalesOrderProjection(LocalDateTime createdAt, BigDecimal totalAmount, Long orderId,
                                String categoryName, BigDecimal orderDetailSellPrice) {
        this.createdAt = createdAt;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
        this.categoryName = categoryName;
        this.orderDetailSellPrice = orderDetailSellPrice;
    }

    /**
     * Obtiene la fecha de la orden como LocalDate para agrupaciones diarias
     */
    public LocalDate getOrderLocalDate() {
        return createdAt != null ? createdAt.toLocalDate() : null;
    }

    /**
     * Obtiene la hora de la orden para análisis de horarios pico
     */
    public Integer getOrderHour() {
        return createdAt != null ? createdAt.getHour() : null;
    }

    /**
     * Obtiene el día de la semana para análisis semanal
     */
    public DayOfWeek getOrderDayOfWeek() {
        return createdAt != null ? createdAt.getDayOfWeek() : null;
    }

    /**
     * Obtiene el monto seguro (nunca null)
     */
    public BigDecimal getSafeTotalAmount() {
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

}