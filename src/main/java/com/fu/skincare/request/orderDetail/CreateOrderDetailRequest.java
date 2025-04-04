package com.fu.skincare.request.orderDetail;

import javax.validation.constraints.Min;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderDetailRequest {
  private int productDetailId;
  @Min(value = 0, message = "Price must be greater than 0")
  private int quantity;
}
