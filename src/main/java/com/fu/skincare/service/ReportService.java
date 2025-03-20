package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.response.report.OrderDetailReport;
import com.fu.skincare.response.report.RevenueReportResponse;

public interface ReportService {
    public List<OrderDetailReport> getOrderDetailReport();

    public RevenueReportResponse getRevenueReport();
}
