package com.mdmc.posofmyheart.application.dtos.projection;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Record concretas para proyecciones de reportes de ventas
 * Todas son clases normales (no abstractas) para permitir instanciación
 */
@Builder
public record SalesReportProjections() {
    /**
     * Proyección para ventas por categoría
     */
    @Builder
    public record CategorySalesProjection(
            String categoryName,
            BigDecimal totalSales,
            Long totalOrders) {
    }

    /**
     * Proyección para resumen de período
     */
    @Builder
    public record PeriodSummaryProjection(
            BigDecimal totalSales,
            Long totalOrders) {
    }

    /**
     * Proyección para ventas diarias
     */
    @Builder
    public record DailySalesProjection(
            LocalDateTime orderDate,
            BigDecimal totalSales,
            Long totalOrders) {
    }
}