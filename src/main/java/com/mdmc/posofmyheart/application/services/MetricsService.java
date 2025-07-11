package com.mdmc.posofmyheart.application.services;

import java.math.BigDecimal;

import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;

public interface MetricsService {
    DailyEarningsResponse getDailyEarnings(Integer backDays);

    DailyEarningsResponse getTodayDailyEarnings();

    DailyEarningsResponse getTodayDailyEarningsWithPercentage(BigDecimal profit);

    //Margen de ganancia por producto
    //Análisis completo por producto
    //Productos con bajo rendimiento
}
