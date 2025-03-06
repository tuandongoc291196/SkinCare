package com.fu.skincare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.bill.BillSuccessMessage;
import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.bill.BillResponse;
import com.fu.skincare.service.BillService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/bill")
@CrossOrigin("*")
@RequiredArgsConstructor
public class BillController {
  private final BillService billService;

  @PostMapping("/create")
  public ResponseEntity<?> createBill(@Validated @RequestBody CreateBillRequest request) {
    ResponseDTO<BillResponse> responseDTO = new ResponseDTO<BillResponse>();
    BillResponse data = billService.createBill(request);
    responseDTO.setData(data);
    responseDTO.setMessage(BillSuccessMessage.CREATE_BILL_SUCCESS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
