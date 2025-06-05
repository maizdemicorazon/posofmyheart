package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;

import java.math.BigDecimal;

public interface MetricsService {
    DailyEarningsResponse getDailyEarnings(Integer backDays);
    DailyEarningsResponse getTodayDailyEarnings();
    DailyEarningsResponse getTodayDailyEarningsWithPercentage(BigDecimal profit);

    //Margen de ganancia por producto
    //Análisis completo por producto
    //Productos con bajo rendimiento
}
