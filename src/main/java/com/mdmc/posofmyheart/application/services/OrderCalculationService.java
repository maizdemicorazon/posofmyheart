package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.domain.dtos.ResultCommissionDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;

import java.math.BigDecimal;
import java.util.Set;

public interface OrderCalculationService {

    // ===== MÉTODOS PARA ORDEN INDIVIDUAL =====

    /**
     * Calcula el total de una orden incluyendo productos y extras
     */
    BigDecimal calculateOrderTotal(OrderEntity order);

    // ===== MÉTODOS PARA MÚLTIPLES ÓRDENES =====

    /**
     * Calcula el monto total de ventas brutas para una Seta de órdenes
     */
    BigDecimal calculateTotalAmount(Set<OrderEntity> orders);

    /**
     * Calcula la ganancia neta de productos (precio de venta - costo de producción)
     */
    BigDecimal calculateNetProductProfit(Set<OrderEntity> orders);

    /**
     * Calcula la ganancia neta de extras (precio de venta - costo de producción)
     */
    BigDecimal calculateNetExtrasProfit(Set<OrderEntity> orders);

    /**
     * Calcula el costo total de producción (productos + extras)
     */
    BigDecimal calculateTotalProductionCost(Set<OrderEntity> orders);

    /**
     * Cuenta el total de items en las órdenes
     */
    int countTotalItems(Set<OrderEntity> orders);

    /**
     * Cuenta el total de extras en las órdenes
     */
    int countTotalExtras(Set<OrderEntity> orders);

    // ===== MÉTODOS PARA CÁLCULOS DE COMISIONES =====

    /**
     * Calcula el resultado de comisiones para pagos con tarjeta
     *
     * @param orders Seta de órdenes a procesar
     * @return ResultCommission con conteo de pagos, descuento total y ventas netas
     */
    ResultCommissionDto calculateCommissionResult(Set<OrderEntity> orders);

    /**
     * Calcula la ganancia real total combinando productos y extras
     *
     * @param orders Seta de órdenes a procesar
     * @return BigDecimal con la ganancia real (productos + extras)
     */
    BigDecimal calculateTotalRealProfit(Set<OrderEntity> orders);

    /**
     * Calcula la ganancia final después de reinversión obligatoria
     *
     * @param orders Seta de órdenes
     * @return objeto FinancialResult con netProfit, mandatoryReinvestment y finalProfit
     */
    DailyEarnings.EarningsSummary calculateFinalProfit(
            Set<OrderEntity> orders,
            BigDecimal profit
    );

    /**
     * Calcula el monto obligatorio a reinvertir (60% del beneficio total)
     */
    BigDecimal calculateReinvestmentAmount(BigDecimal totalProfit, BigDecimal invest);

    /**
     * Calcula la ganancia neta disponible (40% del beneficio total)
     */
    BigDecimal calculateNetProfitAmount(BigDecimal totalProfit, BigDecimal profit);

}