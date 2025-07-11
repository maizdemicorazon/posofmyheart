package com.mdmc.posofmyheart.application.dtos.reports;

import java.math.BigDecimal;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SalesReportResponse {

    private Integer period;
    private BigDecimal totalSales;
    private Integer totalOrders;
    private BigDecimal averageTicket;
    private BigDecimal dailyAverage;
    private Double ordersAverage;
    private Double growthRate;
    private BigDecimal previousPeriodSales;
    private List<DailySalesData> dailyData;
    private List<CategoryAnalysisData> categoryAnalysis;
    private List<WeekdayAnalysisData> weekdayAnalysis;
    private List<PeakHourData> peakHours;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DailySalesData {
        private String date;
        private String fullDate;
        private BigDecimal sales;
        private Integer orders;
        private BigDecimal averageTicket;
        private String dayOfWeek;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CategoryAnalysisData {
        private String name;
        private BigDecimal sales;
        private Integer orders;
        private Double percentage;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class WeekdayAnalysisData {
        private String day;
        private BigDecimal averageSales;
        private Integer averageOrders;
        private BigDecimal totalSales;
        private Integer totalOrders;
    }

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PeakHourData {
        private String hour;
        private BigDecimal sales;
        private Double percentage;
    }
}