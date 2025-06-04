package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;

public interface MetricsService {
    DailyEarningsResponse getDailyEarnings(Integer backDays);
}
