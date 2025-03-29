package com.fu.skincare.response.bill;

import java.util.List;

import com.fu.skincare.response.account.AccountResponse;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillResponse {
  private int id;
  private String createAt;
  private String address;
  private String phoneNumber;
  private int deliveryFee;
  private int totalPrice;
  private String status;
  private String reason;
  private List<OrderDetailResponse> listProducts;
  private AccountResponse account;
}
