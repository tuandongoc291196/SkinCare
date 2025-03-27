package com.fu.skincare.service;

import java.util.List;

import com.fu.skincare.entity.BillHistory;

public interface BillHistoryService {
  public List<BillHistory> getByBill(int billId);
}
