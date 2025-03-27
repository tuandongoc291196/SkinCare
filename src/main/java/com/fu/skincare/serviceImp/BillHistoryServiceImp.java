package com.fu.skincare.serviceImp;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fu.skincare.constants.message.bill.BillErrorMessage;
import com.fu.skincare.entity.Bill;
import com.fu.skincare.entity.BillHistory;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.BillHistoryRepository;
import com.fu.skincare.repository.BillRepository;
import com.fu.skincare.service.BillHistoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BillHistoryServiceImp implements BillHistoryService {

  private final BillHistoryRepository billHistoryRepository;
  private final BillRepository billRepository;

  @Override
  public List<BillHistory> getByBill(int billId) {

    Bill bill = billRepository.findById(billId).orElseThrow(
        () -> new ErrorException(BillErrorMessage.NOT_FOUND));

    return billHistoryRepository.findByBill(bill);

  }

}
