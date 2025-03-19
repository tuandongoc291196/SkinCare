package com.fu.skincare.controller;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.account.AccountSuccessMessage;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("/payments")
@CrossOrigin("*")
@RequiredArgsConstructor
public class PaymentController {
    @Autowired
    PaymentService paymentService;
    @PostMapping(value = "request")
    public ResponseEntity<?> requestPayment(HttpServletRequest request, HttpServletResponse response, @RequestParam("billId") int billId) {
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
