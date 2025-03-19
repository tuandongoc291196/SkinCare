package com.fu.skincare.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public interface PaymentService {
    String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, int billId);

    void paymentResponse(HttpServletRequest req, HttpServletResponse response) throws IOException;
}
