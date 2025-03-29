package com.fu.skincare.constants.message.payments;

public class PaymentConstants {
    public final static String VNP_PAY_URL = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html?";
//     public final static String VNP_RETURN_URL = "http://localhost:8080/payments/call-back";
    public final static String VNP_RETURN_URL = "https://skin-care-api.azurewebsites.net/payments/call-back";
    public final static String VNP_TMN_CODE = "PODT5GPF";
    public final static String VNP_HASH_SECRET = "ERCN2HESH2DZAP4AAC7HY5Q10B5WH448";
    public final static String VNP_VERSION = "2.1.0";
    public final static String VNP_COMMAND_PAY = "pay";
    public final static String VNP_ORDER_TYPE = "topup";
    public final static String VNP_RETURN_CLIENT_URL = "http://localhost:5173/theo-doi-don-hang?status=SUCCESS";
}
