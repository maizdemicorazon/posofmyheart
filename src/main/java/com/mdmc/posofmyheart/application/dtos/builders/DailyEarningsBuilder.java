package com.mdmc.posofmyheart.application.dtos.builders;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.domain.dtos.ResultCommissionDto;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.orders.OrderEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

public class DailyEarningsBuilder {

    public static final BigDecimal DEFAULT_PROFIT = null;

    public static DailyEarnings fromEntry(
            Map.Entry<LocalDate, Set<OrderEntity>> entry,
            OrderCalculationService service
    ){
        return fromEntry(entry, service, DEFAULT_PROFIT);
    }

    public static DailyEarnings fromEntry(
            Map.Entry<LocalDate, Set<OrderEntity>> entry,
            OrderCalculationService service,
            BigDecimal profit
    ) {
        BigDecimal ventaBruta = service.calculateTotalAmount(entry.getValue());
        BigDecimal gananciaProductos = service.calculateNetProductProfit(entry.getValue());
        BigDecimal gananciaExtras = service.calculateNetExtrasProfit(entry.getValue());
        BigDecimal gananciaReal = service.calculateTotalRealProfit(entry.getValue());
        ResultCommissionDto commission = service.calculateCommissionResult(entry.getValue());
        DailyEarnings.EarningsSummary earningsSummary = service.calculateFinalProfit(entry.getValue(), profit);

        return DailyEarnings.builder()
                .withDate(entry.getKey())
                .withCountOrders(entry.getValue().size())
                .withGrossSells(ventaBruta)
                .withNetProfitProduct(gananciaProductos)
                .withNetProfitExtra(gananciaExtras)
                .withCommission(DailyEarnings.Commission.builder()
                        .withCountCardPays(commission.countCardPays())
                        .withTerminalDiscount(commission.terminalDiscount())
                        .withSellTerminal(commission.sellTerminal())
                        .build())
                .withGrossProfit(gananciaReal)
                .withEarningsSummary(
                        DailyEarnings.EarningsSummary.builder()
                                .withReinvestment(earningsSummary.reinvestment())
                                .withNetProfit(earningsSummary.netProfit())
                                .build()
                )
                .build();
    }

}
