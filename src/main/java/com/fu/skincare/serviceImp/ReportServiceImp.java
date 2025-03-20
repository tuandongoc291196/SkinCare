package com.fu.skincare.serviceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fu.skincare.repository.BillRepository;
import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.response.report.OrderDetailReport;
import com.fu.skincare.response.report.RevenueReportByDay;
import com.fu.skincare.response.report.RevenueReportResponse;
import com.fu.skincare.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {

    private final OrderDetailRepository orderDetailRepository;
    private final BillRepository billRepository;

    @Override
    public List<OrderDetailReport> getOrderDetailReport() {

        List<Object[]> orderDetailReports = orderDetailRepository.getOrderDetailReport();
        List<OrderDetailReport> result = orderDetailReports.stream()
                .map(row -> new OrderDetailReport(
                        ((Number) row[0]).intValue(), // id
                        (String) row[1], // name
                        ((Number) row[2]).intValue() // quantity
                ))
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public RevenueReportResponse getRevenueReport() {

        List<Object[]> result = billRepository.getBillReport();
        List<RevenueReportByDay> revenueReportByDays = result.stream()
                .map(row -> new RevenueReportByDay(
                        ((String) row[0].toString()), // date
                        ((Number) row[1]).intValue() // total_price
                ))
                .collect(Collectors.toList());

        int totalRevenue = revenueReportByDays.stream().mapToInt(RevenueReportByDay::getTotalPrice).sum();

        return new RevenueReportResponse(totalRevenue, revenueReportByDays);
    }

}
