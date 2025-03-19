package com.fu.skincare.serviceImp;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.response.report.OrderDetailReport;
import com.fu.skincare.service.ReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ReportServiceImp implements ReportService {

    private final OrderDetailRepository orderDetailRepository;

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

}
