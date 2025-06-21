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
    private LocalDateTime orderDate;

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
     * Método de pago (opcional)
     * Útil para análisis de métodos de pago
     */
    private String paymentMethodName;

    /**
     * Constructor para consultas básicas de órdenes
     * Usado en findAllOrdersInPeriod
     */
    public SalesOrderProjection(LocalDateTime orderDate, BigDecimal totalAmount, Long orderId) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
    }

    /**
     * Constructor para consultas con categoría
     * Usado en análisis por categorías
     */
    public SalesOrderProjection(LocalDateTime orderDate, BigDecimal totalAmount, Long orderId,
                                String categoryName, BigDecimal orderDetailSellPrice) {
        this.orderDate = orderDate;
        this.totalAmount = totalAmount;
        this.orderId = orderId;
        this.categoryName = categoryName;
        this.orderDetailSellPrice = orderDetailSellPrice;
    }

    /**
     * Obtiene la fecha de la orden como LocalDate para agrupaciones diarias
     */
    public LocalDate getOrderLocalDate() {
        return orderDate != null ? orderDate.toLocalDate() : null;
    }

    /**
     * Obtiene la hora de la orden para análisis de horarios pico
     */
    public Integer getOrderHour() {
        return orderDate != null ? orderDate.getHour() : null;
    }

    /**
     * Obtiene el día de la semana para análisis semanal
     */
    public DayOfWeek getOrderDayOfWeek() {
        return orderDate != null ? orderDate.getDayOfWeek() : null;
    }

    /**
     * Verifica si la proyección tiene información de categoría
     */
    public boolean hasCategoryInfo() {
        return categoryName != null && !categoryName.trim().isEmpty();
    }

    /**
     * Verifica si la proyección tiene información de detalle de orden
     */
    public boolean hasOrderDetailInfo() {
        return orderDetailSellPrice != null;
    }

    /**
     * Obtiene el monto seguro (nunca null)
     */
    public BigDecimal getSafeTotalAmount() {
        return totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    /**
     * Obtiene el precio de detalle seguro (nunca null)
     */
    public BigDecimal getSafeOrderDetailSellPrice() {
        return orderDetailSellPrice != null ? orderDetailSellPrice : BigDecimal.ZERO;
    }
}