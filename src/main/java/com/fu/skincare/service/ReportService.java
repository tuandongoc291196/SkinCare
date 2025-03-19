package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.response.report.OrderDetailReport;

public interface ReportService {
    public List<OrderDetailReport> getOrderDetailReport();
}
