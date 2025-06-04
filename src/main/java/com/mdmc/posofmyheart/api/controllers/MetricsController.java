package com.mdmc.posofmyheart.api.controllers;

import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import com.mdmc.posofmyheart.application.services.MetricsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/metrics")
@RequiredArgsConstructor
@Log4j2
public class MetricsController {

    public static final String DAILY_EARNINGS = "/daily-earnings";
    public static final String DAILY_EARNINGS_WITH_BACK_DAYS = DAILY_EARNINGS + "/" + "{backDays}";
    private final MetricsService metricsService;

    @GetMapping(value = {DAILY_EARNINGS, DAILY_EARNINGS + "/"})
    public ResponseEntity<DailyEarningsResponse> getDefaultDailyEarnings() {
        return getDailyEarnings(0);
    }

    @GetMapping(DAILY_EARNINGS_WITH_BACK_DAYS)
    public ResponseEntity<DailyEarningsResponse> getDailyEarnings(@PathVariable Integer backDays) {
        return ResponseEntity.ok(metricsService.getDailyEarnings(backDays));
    }

}
