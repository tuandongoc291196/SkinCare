package com.fu.skincare.service;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface PaymentService {
    String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, int billId);

    void paymentResponse(HttpServletRequest req, HttpServletResponse response) throws IOException;
}
