package com.mdmc.posofmyheart.application.dtos;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.Builder;

@Builder(setterPrefix = "with")
public record DailyEarnings(
        //fecha de consulta
        LocalDate date,
        //numero de ordenes
        int countOrders,
        //venta bruta
        BigDecimal grossSells,
        //ganancia de productos neta
        BigDecimal netProfitProduct,
        //ganancia de extras neta
        BigDecimal netProfitExtra,
        //commission
        Commission commission,
        //ganancia real
        BigDecimal grossProfit,
        EarningsSummary earningsSummary
) {
    @Builder(setterPrefix = "with")
    public record Commission(
            //pagos con tarjeta
            int countCardPays,
            //descuento terminal
            BigDecimal terminalDiscount,
            //venta en terminal
            BigDecimal sellTerminal
    ) {
    }

    @Builder(setterPrefix = "with")
    public record EarningsSummary(
            BigDecimal reinvestment,
            BigDecimal netProfit
    ) {
    }
}
