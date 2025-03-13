package com.fu.skincare.controller;

import com.fu.skincare.response.ListResponseDTO;
import com.fu.skincare.response.bill.BillByAccountResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.bill.BillSuccessMessage;
import com.fu.skincare.request.bill.CreateBillRequest;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.bill.BillResponse;
import com.fu.skincare.service.BillService;

import lombok.RequiredArgsConstructor;

import java.util.List;

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

  @GetMapping("/")
  public ResponseEntity<?> getById(@RequestParam int id) {
    ResponseDTO<BillResponse> responseDTO = new ResponseDTO<>();
    BillResponse data = billService.getById(id);
    responseDTO.setData(data);
    responseDTO.setMessage(BillSuccessMessage.GET_BY_ID);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/getAll")
  public ResponseEntity<?> getAllBy(
          @RequestParam(defaultValue = "0") int pageNo,
          @RequestParam(defaultValue = "10") int pageSize,
          @RequestParam(defaultValue = "id") String sortBy,
          @RequestParam(defaultValue = "true") boolean isAscending) {
    ListResponseDTO<BillResponse> responseDTO = new ListResponseDTO<>();
    List<BillResponse> data = billService.getAll(pageNo, pageSize, sortBy, isAscending);
    responseDTO.setData(data);
    responseDTO.setMessage(BillSuccessMessage.GET_ALL_BILLS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }

  @GetMapping("/getAllByAccount/")
  public ResponseEntity<?> getAllByAccount(@RequestParam int accountId,
          @RequestParam(defaultValue = "0") int pageNo,
          @RequestParam(defaultValue = "10") int pageSize,
          @RequestParam(defaultValue = "id") String sortBy,
          @RequestParam(defaultValue = "true") boolean isAscending) {
    ListResponseDTO<BillByAccountResponse> responseDTO = new ListResponseDTO<>();
    List<BillByAccountResponse> data = billService.getAllByAccountId(accountId,pageNo, pageSize, sortBy, isAscending);
    responseDTO.setData(data);
    responseDTO.setMessage(BillSuccessMessage.GET_ALL_BILLS);
    responseDTO.setStatus(Status.SUCCESS);
    return ResponseEntity.ok().body(responseDTO);
  }
}
