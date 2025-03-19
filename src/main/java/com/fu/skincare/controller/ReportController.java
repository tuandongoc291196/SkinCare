package com.fu.skincare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.report.ReportSuccessMessage;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.report.OrderDetailReport;
import com.fu.skincare.service.ReportService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/report")
@CrossOrigin("*")
@RequiredArgsConstructor
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/order-detail")
    public ResponseEntity<?> getOrderReport() {
        ListResponseDTO<OrderDetailReport> responseDTO = new ListResponseDTO<OrderDetailReport>();
        List<OrderDetailReport> data = reportService.getOrderDetailReport();
        responseDTO.setData(data);
        responseDTO.setMessage(ReportSuccessMessage.ORDER_DETAIL);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

}
