package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.projection.OrderProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections.CategorySalesProjection;
import com.mdmc.posofmyheart.application.dtos.projection.SalesReportProjections.PeriodSummaryProjection;
import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;
import com.mdmc.posofmyheart.application.mappers.SalesReportMapper;
import com.mdmc.posofmyheart.application.services.processors.SalesDataProcessor;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.SalesReportRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Tests unitarios para SalesReportService
 * Demuestra cómo testear la implementación database-agnostic
 */
@ExtendWith(MockitoExtension.class)
class SalesReportServiceTest {

    @Mock
    private SalesReportRepository salesReportRepository;

    @Mock
    private SalesCriteriaService salesCriteriaService;

    @Mock
    private SalesDataProcessor salesDataProcessor;

    @Mock
    private SalesReportMapper salesReportMapper;

    @Mock
    private SalesReportService salesReportService;

    private List<OrderProjection> testOrders;
    private List<CategorySalesProjection> testCategories;
    private PeriodSummaryProjection currentPeriod;
    private PeriodSummaryProjection previousPeriod;

    @BeforeEach
    void setUp() {
        setupTestData();
    }

    @Test
    void shouldGenerateSalesReportSuccessfully() {
        // Given
        Integer days = 7;
        mockRepositoryResponses();
        mockProcessorResponses();

        // When
        SalesReportResponse result = salesReportService.generateSalesReport(days);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPeriod()).isEqualTo(days);
        assertThat(result.getTotalSales()).isEqualTo(BigDecimal.valueOf(5000));
        assertThat(result.getTotalOrders()).isEqualTo(50);
        assertThat(result.getAverageTicket()).isEqualTo(BigDecimal.valueOf(100.00));
    }

    @Test
    void shouldCalculateGrowthRateCorrectly() {
        // Given
        Integer days = 7;
        mockRepositoryResponses();
        mockProcessorResponses();

        // When
        SalesReportResponse result = salesReportService.generateSalesReport(days);

        // Then
        // Current: 5000, Previous: 4000 = 25% growth
        assertThat(result.getGrowthRate()).isEqualTo(25.0);
    }

    @Test
    void shouldHandleZeroPreviousSales() {
        // Given
        Integer days = 7;
        previousPeriod = new PeriodSummaryProjection(BigDecimal.ZERO, 0L);
        mockRepositoryResponses();
        mockProcessorResponses();

        // When
        SalesReportResponse result = salesReportService.generateSalesReport(days);

        // Then
        assertThat(result.getGrowthRate()).isEqualTo(0.0);
    }

    @Test
    void shouldCalculateDailyAverageCorrectly() {
        // Given
        Integer days = 10;
        mockRepositoryResponses();
        mockProcessorResponses();

        // When
        SalesReportResponse result = salesReportService.generateSalesReport(days);

        // Then
        // 5000 / 10 days = 500 per day
        assertThat(result.getDailyAverage()).isEqualTo(BigDecimal.valueOf(500.00));
    }

    private void setupTestData() {
        LocalDateTime now = LocalDateTime.now();

        testOrders = List.of(
                new OrderProjection(now.minusDays(1), BigDecimal.valueOf(100), 1L, "Esquites", null),
                new OrderProjection(now.minusDays(2), BigDecimal.valueOf(150), 2L, "Elotes", null),
                new OrderProjection(now.minusDays(3), BigDecimal.valueOf(80), 3L, "Bebidas", null)
        );

        testCategories = List.of(
                new CategorySalesProjection("Esquites", BigDecimal.valueOf(2500), 25L),
                new CategorySalesProjection("Elotes", BigDecimal.valueOf(2000), 20L),
                new CategorySalesProjection("Bebidas", BigDecimal.valueOf(500), 5L)
        );

        currentPeriod = new PeriodSummaryProjection(BigDecimal.valueOf(5000), 50L);
        previousPeriod = new PeriodSummaryProjection(BigDecimal.valueOf(4000), 40L);
    }

    private void mockRepositoryResponses() {
        when(salesReportRepository.findPeriodSummary(any(), any()))
                .thenReturn(currentPeriod)
                .thenReturn(previousPeriod);

        when(salesReportRepository.findAllOrdersInPeriod(any(), any()))
                .thenReturn(testOrders);

        when(salesReportRepository.findCategorySalesInPeriod(any(), any()))
                .thenReturn(testCategories);
    }

    private void mockProcessorResponses() {
        when(salesDataProcessor.processDailySalesData(any()))
                .thenReturn(List.of()); // Mock empty for simplicity

        when(salesDataProcessor.processWeekdayAnalysis(any()))
                .thenReturn(List.of()); // Mock empty for simplicity

        when(salesDataProcessor.processPeakHours(any(), any()))
                .thenReturn(List.of()); // Mock empty for simplicity

        when(salesDataProcessor.processCategories(any(), any()))
                .thenReturn(List.of()); // Mock empty for simplicity
    }
}