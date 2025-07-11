package com.mdmc.posofmyheart.api.controllers;

import java.time.LocalDateTime;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;
import com.mdmc.posofmyheart.application.services.SalesReportService;

@RestController
@RequestMapping("/reports")
@RequiredArgsConstructor
@Log4j2
@Validated
public class SalesReportController {

    private final SalesReportService salesReportService;

    @GetMapping("/sales/{days}")
    public ResponseEntity<SalesReportResponse> getSalesReport(
            @PathVariable
            @Min(value = 1, message = "Days must be at least 1")
            @Max(value = 365, message = "Days must not exceed 365")
            Integer days) {

        log.info("Received request for sales report with {} days", days);

        try {
            SalesReportResponse report = salesReportService.generateSalesReport(days);
            log.info("Successfully generated sales report for {} days", days);

            return ResponseEntity.ok()
                    .header("X-Report-Generated", LocalDateTime.now().toString())
                    .body(report);

        } catch (Exception e) {
            log.error("Error generating sales report for {} days: {}", days, e.getMessage(), e);
            return ResponseEntity.internalServerError()
                    .header("X-Error-Message", e.getMessage())
                    .build();
        }
    }
}