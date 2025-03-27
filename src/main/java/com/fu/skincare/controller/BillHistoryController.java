package com.fu.skincare.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.bill.BillSuccessMessage;
import com.fu.skincare.entity.BillHistory;
import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.service.BillHistoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bill-history")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillHistoryController {
  private final BillHistoryService billHistoryService;

  @GetMapping("/")
  public ResponseEntity<?> getById(@RequestParam int billId) {
    ListResponseDTO<BillHistory> responseDTO = new ListResponseDTO<BillHistory>();
    List<BillHistory> data = billHistoryService.getByBill(billId);
    responseDTO.setData(data);
    responseDTO.setMessage(BillSuccessMessage.GET_BILL_HISTORY);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
