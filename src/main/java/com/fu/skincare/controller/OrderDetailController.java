package com.fu.skincare.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.orderDetail.OrderDetailSuccessMessage;
import com.fu.skincare.response.ResponseDTO;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import com.fu.skincare.service.OrderDetailService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/orderDetail")
@CrossOrigin("*")
@RequiredArgsConstructor
public class OrderDetailController {
    private final OrderDetailService orderDetailService;

    @GetMapping("/")
    public ResponseEntity<?> getById(@RequestParam int id) {
        ResponseDTO<OrderDetailResponse> responseDTO = new ResponseDTO<OrderDetailResponse>();
        OrderDetailResponse data = orderDetailService.getOrderDetailById(id);
        responseDTO.setData(data);
        responseDTO.setMessage(OrderDetailSuccessMessage.GET_BY_ID);
        responseDTO.setStatus(Status.SUCCESS);
        return ResponseEntity.ok().body(responseDTO);
    }
}
