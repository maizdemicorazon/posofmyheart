package com.mdmc.posofmyheart.application.services;

import com.mdmc.posofmyheart.application.dtos.reports.SalesReportResponse;

public interface SalesReportService {
    SalesReportResponse generateSalesReport(Integer days);
}
