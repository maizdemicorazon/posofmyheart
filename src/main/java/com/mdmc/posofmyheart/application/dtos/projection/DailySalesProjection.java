package com.mdmc.posofmyheart.application.dtos.projection;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Proyección concreta para ventas diarias
 * Clase independiente para evitar problemas de instanciación
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DailySalesProjection {

    private LocalDateTime orderDate;
    private BigDecimal totalSales;
    private Long totalOrders;

    /**
     * Obtiene la fecha como LocalDate para agrupaciones
     */
    public LocalDate getOrderLocalDate() {
        return orderDate != null ? orderDate.toLocalDate() : null;
    }

    /**
     * Obtiene las ventas totales seguras (nunca null)
     */
    public BigDecimal getSafeTotalSales() {
        return totalSales != null ? totalSales : BigDecimal.ZERO;
    }

    /**
     * Obtiene el total de órdenes seguro (nunca null)
     */
    public Long getSafeTotalOrders() {
        return totalOrders != null ? totalOrders : 0L;
    }

    /**
     * Verifica si tiene fecha válida
     */
    public boolean hasValidDate() {
        return orderDate != null;
    }
}