package com.fu.skincare.response.orderDetail;

import com.fu.skincare.response.product.ProductResponse;

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
public class OrderDetailResponse {
  private int id;
  private String createAt;
  private int quantity;
  private String status;
  private int price;
  private ProductResponse productResponse;
}
