package com.mdmc.posofmyheart.api.controllers;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import com.mdmc.posofmyheart.application.services.MetricsService;

@RestController
@RequestMapping("/metrics")
@RequiredArgsConstructor
public class MetricsController {

    public static final String DAILY_EARNINGS = "/daily-earnings";

    private final MetricsService metricsService;

    @GetMapping(value = {DAILY_EARNINGS, DAILY_EARNINGS + "/"})
    public ResponseEntity<DailyEarningsResponse> getDefaultDailyEarnings() {
        return ResponseEntity.ok(metricsService.getTodayDailyEarnings());
    }

    @GetMapping(DAILY_EARNINGS + "/{backDays}")
    public ResponseEntity<DailyEarningsResponse> getDailyEarnings(@PathVariable Integer backDays) {
        return ResponseEntity.ok(metricsService.getDailyEarnings(backDays));
    }

    @GetMapping(value = {"/summary/{profitPercentage}"})
    public ResponseEntity<DailyEarningsResponse> getDefaultDailyEarningsWithPercentage(
            @PathVariable BigDecimal profitPercentage
    ) {
        return ResponseEntity.ok(metricsService.getTodayDailyEarningsWithPercentage(profitPercentage));
    }

}
