package com.fu.skincare.service;

import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.response.bill.BillResponse;

public interface BillService {
  public BillResponse createBill(CreateBillRequest request);
}
