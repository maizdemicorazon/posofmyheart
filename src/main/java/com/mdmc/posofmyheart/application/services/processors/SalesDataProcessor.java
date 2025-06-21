package com.mdmc.posofmyheart.application.services.processors;

import com.mdmc.posofmyheart.application.dtos.projections.SalesOrderProjection;
import com.mdmc.posofmyheart.application.dtos.projections.SalesReportProjections;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;

import java.math.BigDecimal;
import java.util.List;

public interface SalesDataProcessor {
    List<SalesReportResponse.DailySalesData> processDailySalesData(List<SalesOrderProjection> orders);

    List<SalesReportResponse.WeekdayAnalysisData> processWeekdayAnalysis(List<SalesOrderProjection> orders);

    List<SalesReportResponse.PeakHourData> processPeakHours(List<SalesOrderProjection> orders, BigDecimal totalSales);

    List<SalesReportResponse.CategoryAnalysisData> processCategories(
            List<SalesReportProjections.CategorySalesProjection> categories, BigDecimal totalSales);
}
