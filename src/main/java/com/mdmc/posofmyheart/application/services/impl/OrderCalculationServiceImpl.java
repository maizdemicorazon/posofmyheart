package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.domain.dtos.ResultCommission;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderDetailEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderExtraDetailEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class OrderCalculationServiceImpl implements OrderCalculationService {

    public static final String DIVIDEND = "100";
    // ===== Properties =====
    @Value("${terminal.commission}")
    private BigDecimal terminalCommission;
    @Value("${card.method.id}")
    private Long cardPaymentId;
    @Value("${daily.earnings.summary.invest}")
    private BigDecimal reinvestmentPercentage;
    @Value("${daily.earnings.summary.profit}")
    private BigDecimal profitPercentage;

    // ===== MÉTODOS PARA ORDEN INDIVIDUAL =====

    public BigDecimal calculateOrderTotal(OrderEntity order) {
        return safeGetOrderDetails(order)
                .stream()
                .filter(Objects::nonNull)
                .map(this::calculateDetailTotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateDetailTotal(OrderDetailEntity detail) {
        BigDecimal extrasTotal = calculateExtrasTotal(detail);
        BigDecimal sellPrice = Optional.ofNullable(detail.getSellPrice())
                .orElse(BigDecimal.ZERO);
        return extrasTotal.add(sellPrice);
    }

    private BigDecimal calculateExtrasTotal(OrderDetailEntity detail) {
        return safeGetExtraDetails(detail)
                .stream()
                .filter(Objects::nonNull)
                .filter(extra -> extra.getSellPrice() != null)
                .map(this::calculateExtraPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateExtraPrice(OrderExtraDetailEntity extra) {
        BigDecimal price = extra.getSellPrice();
        int quantity = Optional.ofNullable(extra.getQuantity()).orElse(0);
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    // ===== MÉTODOS PARA MÚLTIPLES ÓRDENES (METRICS) =====

    public BigDecimal calculateTotalAmount(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .map(OrderEntity::getTotalAmount)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateNetProductProfit(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .map(this::calculateProductProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateNetExtrasProfit(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .flatMap(detail -> safeGetExtraDetails(detail).stream())
                .filter(Objects::nonNull)
                .map(this::calculateExtraProfit)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // ===== MÉTODOS DE CÁLCULO DE GANANCIAS =====

    private BigDecimal calculateProductProfit(OrderDetailEntity detail) {
        BigDecimal sellPrice = Optional.ofNullable(detail.getSellPrice()).orElse(BigDecimal.ZERO);
        BigDecimal productionCost = Optional.ofNullable(detail.getProductionCost()).orElse(BigDecimal.ZERO);
        return sellPrice.subtract(productionCost);
    }

    private BigDecimal calculateExtraProfit(OrderExtraDetailEntity extra) {
        BigDecimal sellPrice = Optional.ofNullable(extra.getSellPrice()).orElse(BigDecimal.ZERO);
        BigDecimal productionCost = Optional.ofNullable(extra.getProductionCost()).orElse(BigDecimal.ZERO);
        return sellPrice.subtract(productionCost);
    }

    // ===== MÉTODOS UTILITARIOS PARA MANEJO SEGURO DE NULLS =====

    private List<OrderDetailEntity> safeGetOrderDetails(OrderEntity order) {
        return Optional.ofNullable(order)
                .map(OrderEntity::getOrderDetails)
                .orElse(Collections.emptyList());
    }

    private List<OrderExtraDetailEntity> safeGetExtraDetails(OrderDetailEntity detail) {
        return Optional.ofNullable(detail)
                .map(OrderDetailEntity::getExtraDetails)
                .orElse(Collections.emptyList());
    }

    // ===== MÉTODOS ADICIONALES PARA ANÁLISIS =====

    public BigDecimal calculateTotalProductionCost(List<OrderEntity> orders) {
        BigDecimal productCosts = calculateTotalProductionCostForProducts(orders);
        BigDecimal extrasCosts = calculateTotalProductionCostForExtras(orders);
        return productCosts.add(extrasCosts);
    }

    private BigDecimal calculateTotalProductionCostForProducts(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .map(detail -> Optional.ofNullable(detail.getProductionCost()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal calculateTotalProductionCostForExtras(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .flatMap(detail -> safeGetExtraDetails(detail).stream())
                .filter(Objects::nonNull)
                .map(extra -> Optional.ofNullable(extra.getProductionCost()).orElse(BigDecimal.ZERO))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public int countTotalItems(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .mapToInt(detail -> 1)
                .sum();
    }

    public int countTotalExtras(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .flatMap(order -> safeGetOrderDetails(order).stream())
                .filter(Objects::nonNull)
                .flatMap(detail -> safeGetExtraDetails(detail).stream())
                .filter(Objects::nonNull)
                .mapToInt(extra -> Optional.ofNullable(extra.getQuantity()).orElse(0))
                .sum();
    }

    // ===== MÉTODOS PARA CÁLCULOS DE COMISIONES =====

    public ResultCommission calculateCommissionResult(List<OrderEntity> orders) {
        List<OrderEntity> cardPayments = filterCardPayments(orders);

        List<BigDecimal> commissions = cardPayments.stream()
                .map(this::calculateCommission)
                .toList();

        List<BigDecimal> salesMinusCommissions = cardPayments.stream()
                .map(order -> order.getTotalAmount().subtract(calculateCommission(order)))
                .toList();

        BigDecimal totalDiscount = sumBigDecimalList(commissions);
        BigDecimal totalTerminalSales = sumBigDecimalList(salesMinusCommissions);

        return new ResultCommission(cardPayments.size(), totalDiscount, totalTerminalSales);
    }

    private List<OrderEntity> filterCardPayments(List<OrderEntity> orders) {
        return orders.stream()
                .filter(Objects::nonNull)
                .filter(order -> order.getPaymentMethod() != null)
                .filter(order -> cardPaymentId.equals(order.getPaymentMethod().getIdPayment()))
                .toList();
    }

    private BigDecimal calculateCommission(OrderEntity order) {
        return Optional.ofNullable(order.getTotalAmount())
                .orElse(BigDecimal.ZERO)
                .multiply(terminalCommission)
                .setScale(2, RoundingMode.HALF_UP);
    }

    private BigDecimal sumBigDecimalList(List<BigDecimal> values) {
        return values.stream()
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateTotalRealProfit(List<OrderEntity> orders) {
        BigDecimal gananciaDeProductosNeta = calculateNetProductProfit(orders);
        BigDecimal gananciaExtrasNeta = calculateNetExtrasProfit(orders);
        return gananciaDeProductosNeta.add(gananciaExtrasNeta);
    }

// ===== MÉTODOS PARA CÁLCULO DE GANANCIAS FINALES =====

    public DailyEarnings.EarningsSummary calculateFinalProfit(List<OrderEntity> orders, BigDecimal profit) {
        BigDecimal totalProfit = calculateTotalRealProfit(orders);
        BigDecimal fullPercentage = new BigDecimal(DIVIDEND);
        BigDecimal invest = fullPercentage.subtract(Optional.ofNullable(profit).orElse(BigDecimal.ZERO));

        return DailyEarnings.EarningsSummary.builder()
                .withReinvestment(calculateReinvestmentAmount(totalProfit, (invest)))
                .withNetProfit(calculateNetProfitAmount(totalProfit, profit))
                .build();
    }

    public BigDecimal calculateReinvestmentAmount(BigDecimal totalProfit, BigDecimal invest) {

        BigDecimal percentageToUse = Optional.ofNullable(invest)
                .orElse(reinvestmentPercentage);

        BigDecimal multiplicand = percentageToUse
                .divide(new BigDecimal(DIVIDEND), 2, RoundingMode.HALF_UP);

        return totalProfit.multiply(multiplicand)
                .setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal calculateNetProfitAmount(BigDecimal totalProfit, BigDecimal profit) {
        BigDecimal percentageToUse = Optional.ofNullable(profit)
                .orElse(reinvestmentPercentage);

        BigDecimal multiplicand = percentageToUse
                .divide(new BigDecimal(DIVIDEND), 2, RoundingMode.HALF_UP);

        return totalProfit.multiply(multiplicand)
                .setScale(2, RoundingMode.HALF_UP);
    }
}