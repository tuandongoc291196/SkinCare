package com.fu.skincare.service;

import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.response.bill.BillByAccountResponse;
import com.fu.skincare.response.bill.BillResponse;

import java.util.List;

public interface BillService {
  public BillResponse createBill(CreateBillRequest request);

  public BillResponse getById(int id);

  public BillResponse cancelBill(int id, String reason);

  public BillResponse rejectBill(int id, String reason);

  public BillResponse approvedBill(int id);

  public BillResponse doneBill(int id, String reason);

  public List<BillResponse> getAll(int pageNo, int pageSize, String sortBy, boolean isAscending);

  public List<BillByAccountResponse> getAllByAccountId(int accountId, int pageNo, int pageSize, String sortBy,
      boolean isAscending);
}
