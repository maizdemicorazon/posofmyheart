package com.mdmc.posofmyheart.application.dtos;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record DailyEarnings(
        //fecha de consulta
        LocalDate date,
        //numero de ordenes
        int countOrders,
        //venta bruta
        BigDecimal bruteSell,
        //ganancia de productos neta
        BigDecimal netProfitProduct,
        //ganancia de extras neta
        BigDecimal netProfitExtra,
        //commission
        Commission commission,
        //ganancia real
        BigDecimal realProfit
) {
    @Builder
    public record Commission(
            //pagos con tarjeta
            int countCardPays,
            //descuento terminal
            BigDecimal terminalDiscount,
            //venta en terminal
            BigDecimal sellTerminal
    ) {

    }
}
