package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.dtos.projections.SalesOrderProjection;
import com.mdmc.posofmyheart.application.dtos.projections.SalesReportProjections;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.CategoryAnalysisData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.DailySalesData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.PeakHourData;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse.WeekdayAnalysisData;
import com.mdmc.posofmyheart.application.services.SalesReportService;
import com.mdmc.posofmyheart.application.services.processors.SalesDataProcessor;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.SalesReportRepository;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Servicio de reportes de ventas usando solo JPQL y procesamiento en Java
 * Mantiene independencia total de base de datos
 */
@Service
@AllArgsConstructor
@Log4j2
public class SalesReportServiceImpl implements SalesReportService {

    private final SalesReportRepository salesReportRepository;
    private final SalesDataProcessor salesDataProcessor;

    @Override
    public SalesReportResponse generateSalesReport(Integer days) {
        log.info("Generating sales report for {} days", days);

        LocalDateTime endDate = LocalDateTime.now();
        LocalDateTime startDate = endDate.minusDays(days);
        LocalDateTime previousStartDate = startDate.minusDays(days);

        // Obtener datos del período actual usando JPQL
        SalesReportProjections.PeriodSummaryProjection currentPeriod = salesReportRepository.findPeriodSummary(startDate, endDate);
        SalesReportProjections.PeriodSummaryProjection previousPeriod = salesReportRepository.findPeriodSummary(previousStartDate, startDate);

        // Obtener órdenes detalladas para procesamiento
        List<SalesOrderProjection> currentOrders = salesReportRepository.findAllOrdersInPeriod(startDate, endDate);

        // Obtener análisis por categorías usando JPQL
        List<SalesReportProjections.CategorySalesProjection> categoryData = salesReportRepository.findCategorySalesInPeriod(startDate, endDate);

        return buildSalesReport(days, currentPeriod, previousPeriod, currentOrders, categoryData);
    }

    private SalesReportResponse buildSalesReport(
            Integer days,
            SalesReportProjections.PeriodSummaryProjection currentPeriod,
            SalesReportProjections.PeriodSummaryProjection previousPeriod,
            List<SalesOrderProjection> orders,
            List<SalesReportProjections.CategorySalesProjection> categoryData) {

        BigDecimal totalSales = currentPeriod.totalSales();
        Long totalOrdersLong = currentPeriod.totalOrders();
        Integer totalOrders = totalOrdersLong.intValue();
        BigDecimal previousSales = previousPeriod.totalSales();

        log.debug("Building sales report: totalSales={}, totalOrders={}, previousSales={}",
                totalSales, totalOrders, previousSales);

        return SalesReportResponse.builder()
                .period(days)
                .totalSales(totalSales)
                .totalOrders(totalOrders)
                .averageTicket(calculateAverageTicket(totalSales, totalOrdersLong))
                .dailyAverage(totalSales.divide(BigDecimal.valueOf(days), 2, RoundingMode.HALF_UP))
                .ordersAverage(totalOrdersLong.doubleValue() / days)
                .growthRate(calculateGrowthRate(totalSales, previousSales))
                .previousPeriodSales(previousSales)
                .dailyData(buildDailyData(orders))
                .categoryAnalysis(buildCategoryAnalysis(categoryData, totalSales))
                .weekdayAnalysis(buildWeekdayAnalysis(orders))
                .peakHours(buildPeakHoursData(orders, totalSales))
                .build();
    }

    private List<DailySalesData> buildDailyData(List<SalesOrderProjection> orders) {
        log.debug("Building daily data from {} orders", orders.size());
        return salesDataProcessor.processDailySalesData(orders);
    }

    private List<CategoryAnalysisData> buildCategoryAnalysis(
            List<SalesReportProjections.CategorySalesProjection> categoryData, BigDecimal totalSales) {

        log.debug("Building category analysis from {} categories", categoryData.size());
        return salesDataProcessor.processCategories(categoryData, totalSales);
    }

    private List<WeekdayAnalysisData> buildWeekdayAnalysis(List<SalesOrderProjection> orders) {
        log.debug("Building weekday analysis from {} orders", orders.size());
        return salesDataProcessor.processWeekdayAnalysis(orders);
    }

    private List<PeakHourData> buildPeakHoursData(List<SalesOrderProjection> orders, BigDecimal totalSales) {
        log.debug("Building peak hours data from {} orders", orders.size());
        return salesDataProcessor.processPeakHours(orders, totalSales);
    }

    // Métodos de cálculo auxiliares

    private BigDecimal calculateAverageTicket(BigDecimal totalSales, Long totalOrders) {
        if (totalOrders == 0) return BigDecimal.ZERO;
        return totalSales.divide(BigDecimal.valueOf(totalOrders), 2, RoundingMode.HALF_UP);
    }

    private Double calculateGrowthRate(BigDecimal currentSales, BigDecimal previousSales) {
        return Optional.ofNullable(previousSales).map(
                ps -> {
                    return currentSales.subtract(previousSales)
                            .divide(previousSales, 4, RoundingMode.HALF_UP)
                            .multiply(BigDecimal.valueOf(100))
                            .doubleValue();
                }
        ).orElse(0.0);
    }
}