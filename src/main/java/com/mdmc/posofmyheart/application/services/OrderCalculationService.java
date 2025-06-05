package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.domain.dtos.ResultCommission;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;

import java.math.BigDecimal;
import java.util.List;

public interface OrderCalculationService {

    // ===== MÉTODOS PARA ORDEN INDIVIDUAL =====

    /**
     * Calcula el total de una orden incluyendo productos y extras
     */
    BigDecimal calculateOrderTotal(OrderEntity order);

    // ===== MÉTODOS PARA MÚLTIPLES ÓRDENES =====

    /**
     * Calcula el monto total de ventas brutas para una lista de órdenes
     */
    BigDecimal calculateTotalAmount(List<OrderEntity> orders);

    /**
     * Calcula la ganancia neta de productos (precio de venta - costo de producción)
     */
    BigDecimal calculateNetProductProfit(List<OrderEntity> orders);

    /**
     * Calcula la ganancia neta de extras (precio de venta - costo de producción)
     */
    BigDecimal calculateNetExtrasProfit(List<OrderEntity> orders);

    /**
     * Calcula el costo total de producción (productos + extras)
     */
    BigDecimal calculateTotalProductionCost(List<OrderEntity> orders);

    /**
     * Cuenta el total de items en las órdenes
     */
    int countTotalItems(List<OrderEntity> orders);

    /**
     * Cuenta el total de extras en las órdenes
     */
    int countTotalExtras(List<OrderEntity> orders);

    // ===== MÉTODOS PARA CÁLCULOS DE COMISIONES =====

    /**
     * Calcula el resultado de comisiones para pagos con tarjeta
     *
     * @param orders Lista de órdenes a procesar
     * @return ResultCommission con conteo de pagos, descuento total y ventas netas
     */
    ResultCommission calculateCommissionResult(List<OrderEntity> orders);

    /**
     * Calcula la ganancia real total combinando productos y extras
     *
     * @param orders Lista de órdenes a procesar
     * @return BigDecimal con la ganancia real (productos + extras)
     */
    BigDecimal calculateTotalRealProfit(List<OrderEntity> orders);

    /**
     * Calcula la ganancia final después de reinversión obligatoria
     *
     * @param orders Lista de órdenes
     * @return objeto FinancialResult con netProfit, mandatoryReinvestment y finalProfit
     */
    DailyEarnings.EarningsSummary calculateFinalProfit(
            List<OrderEntity> orders,
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