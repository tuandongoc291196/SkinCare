package com.fu.skincare.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.service.PaymentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    PaymentService paymentService;

    @PostMapping(value = "request")
    public ResponseEntity<?> requestPayment(HttpServletRequest request, HttpServletResponse response,
            @RequestParam("billId") int billId) {
        String paymentURL = paymentService.requestPaymentVNP(request, response, billId);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setData(paymentURL);
        responseDTO.setMessage("Get url payment success");
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping(value = "call-back")
    public ResponseEntity<?> payment(HttpServletRequest request, HttpServletResponse response) throws IOException {
        paymentService.paymentResponse(request, response);
        ResponseDTO<String> responseDTO = new ResponseDTO<>();
        responseDTO.setData(null);
        responseDTO.setMessage("Payment success");
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
