package com.mdmc.posofmyheart.application.services.processors;

import com.mdmc.posofmyheart.application.dtos.projection.OrderProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SalesDataProcessor {
    List<SalesReportResponse.DailySalesData> processDailySalesData(List<OrderProjection> orders);

    List<SalesReportResponse.WeekdayAnalysisData> processWeekdayAnalysis(List<OrderProjection> orders);

    List<SalesReportResponse.PeakHourData> processPeakHours(List<OrderProjection> orders, BigDecimal totalSales);

    List<SalesReportResponse.CategoryAnalysisData> processCategories(
            List<SalesReportProjections.CategorySalesProjection> categories, BigDecimal totalSales);
}
