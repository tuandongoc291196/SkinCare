package com.fu.skincare.serviceImp;

import com.fu.skincare.constants.PaymentStatus;
import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.payments.PaymentConstants;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.Transaction;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.BillRepository;
import com.fu.skincare.response.transaction.TransactionRepository;
import com.fu.skincare.service.PaymentService;
import com.fu.skincare.shared.Utils;
import com.fu.skincare.shared.VNPayUtil;
import jdk.jshell.execution.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PaymentServiceImp implements PaymentService {

    @Autowired
    BillRepository billRepository;

    @Autowired
    TransactionRepository transactionRepository;

    @Override
    @Transactional
    public String requestPaymentVNP(HttpServletRequest req, HttpServletResponse resp, int billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ErrorException("Bill Id not exist"));
        if(!bill.getStatus().equals(Status.PENDING)){
            throw new ErrorException("Bill is not in payment process");
        }
        String vnp_TxnRef = String.valueOf(System.currentTimeMillis());
        String vnp_IpAddr = req.getRemoteAddr();
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", PaymentConstants.VNP_VERSION);
        vnp_Params.put("vnp_Command", PaymentConstants.VNP_COMMAND_PAY);
        vnp_Params.put("vnp_TmnCode", PaymentConstants.VNP_TMN_CODE);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderType", PaymentConstants.VNP_ORDER_TYPE);

        vnp_Params.put("vnp_OrderInfo", billId+"");
        vnp_Params.put("vnp_Amount", String.valueOf(bill.getTotalPrice() * 100L));
        vnp_Params.put("vnp_ReturnUrl", PaymentConstants.VNP_RETURN_URL);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        cld.add(Calendar.HOUR_OF_DAY, 7);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = (String) itr.next();
            String fieldValue = (String) vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = VNPayUtil.hmacSHA512(PaymentConstants.VNP_HASH_SECRET, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        createTransaction(bill);
        return PaymentConstants.VNP_PAY_URL + queryUrl;
    }

    @Override
    @Transactional
    public void paymentResponse(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String billId = request.getParameter("vnp_OrderInfo");
        Transaction transaction =  transactionRepository.findByBillId(Integer.parseInt(billId))
                .orElseThrow(() -> new ErrorException("Transaction not exist"));
        if (!"00".equals(request.getParameter("vnp_ResponseCode"))) {
            transaction.setStatus(PaymentStatus.FAILURE);
            transactionRepository.save(transaction);
            response.sendRedirect(String.format(PaymentConstants.VNP_RETURN_CLIENT_URL));
        } else {
            transaction.setStatus(PaymentStatus.PAID);
            transactionRepository.save(transaction);
            Bill bill = billRepository.findById(Integer.parseInt(billId))
                    .orElseThrow(() -> new ErrorException("Bill not exist"));
            bill.setStatus(Status.SUCCESS);
            billRepository.save(bill);
            response.sendRedirect(String.format(PaymentConstants.VNP_RETURN_CLIENT_URL));
        }
    }

    private void createTransaction(Bill bill){
        Transaction transaction = Transaction.builder()
                .amount(bill.getTotalPrice())
                .createAt(Utils.formatVNDatetimeNow())
                .status(PaymentStatus.PROCESSING)
                .bill(bill)
                .build();
        transactionRepository.saveAndFlush(transaction);
    }
}
