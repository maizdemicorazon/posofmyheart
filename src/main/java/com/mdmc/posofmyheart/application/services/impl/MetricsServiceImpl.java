package com.mdmc.posofmyheart.application.services.impl;

import com.mdmc.posofmyheart.application.dtos.DailyEarnings;
import com.mdmc.posofmyheart.application.dtos.DailyEarningsResponse;
import com.mdmc.posofmyheart.application.mappers.DailyEarningsResponseMapper;
import com.mdmc.posofmyheart.application.services.MetricsService;
import com.mdmc.posofmyheart.application.services.OrderCalculationService;
import com.mdmc.posofmyheart.domain.dtos.ResultCommission;
import com.mdmc.posofmyheart.infrastructure.persistence.entities.OrderEntity;
import com.mdmc.posofmyheart.infrastructure.persistence.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class MetricsServiceImpl implements MetricsService {

    private final OrderRepository orderRepository;
    private final OrderCalculationService orderCalculationService;

    @Override
    public DailyEarningsResponse getDailyEarnings(Integer backDays) {
        LocalDateTime startDate = LocalDateTime.of(LocalDate.now().minusDays(backDays), LocalTime.MIN);
        List<OrderEntity> orders = orderRepository.findByDateRange(startDate, LocalDateTime.now());

        List<DailyEarnings> dailyEarnings = groupOrdersByDate(orders)
                .entrySet()
                .stream()
                .map(this::createDailyEarnings)
                .sorted(Comparator.comparing(DailyEarnings::date).reversed())
                .toList();

        return DailyEarningsResponseMapper.INSTANCE.toResponse(dailyEarnings);
    }

    private Map<LocalDate, List<OrderEntity>> groupOrdersByDate(List<OrderEntity> orders) {
        return orders.stream()
                .collect(Collectors.groupingBy(
                        order -> order.getOrderDate().toLocalDate()
                ));
    }

    private DailyEarnings createDailyEarnings(Map.Entry<LocalDate, List<OrderEntity>> entry) {
        BigDecimal ventaBruta = orderCalculationService.calculateTotalAmount(entry.getValue());
        BigDecimal gananciaDeProductosNeta = orderCalculationService.calculateNetProductProfit(entry.getValue());
        BigDecimal gananciaExtrasNeta = orderCalculationService.calculateNetExtrasProfit(entry.getValue());
        BigDecimal gananciaReal = orderCalculationService.calculateTotalRealProfit(entry.getValue());
        ResultCommission resultCommission = orderCalculationService.calculateCommissionResult(entry.getValue());

        return DailyEarnings
                .builder()
                .date(entry.getKey())
                .countOrders(entry.getValue().size())
                .bruteSell(ventaBruta)
                .netProfitProduct(gananciaDeProductosNeta)
                .netProfitExtra(gananciaExtrasNeta)
                .commission(DailyEarnings.Commission
                        .builder()
                        .countCardPays(resultCommission.countCardPays())
                        .terminalDiscount(resultCommission.terminalDiscount())
                        .sellTerminal(resultCommission.sellTerminal())
                        .build())
                .realProfit(gananciaReal)
                .build();
    }

}