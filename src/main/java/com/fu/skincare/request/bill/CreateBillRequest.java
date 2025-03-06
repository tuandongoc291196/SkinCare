package com.fu.skincare.request.bill;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.fu.skincare.constants.message.validation.ValidationMessage;
import com.fu.skincare.custom.customAnnotation.ValidPhoneNumber;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CreateBillRequest {
  private int accountId;
  @NotNull(message = ValidationMessage.ADDRESS_NOT_NULL)
  private String address;
  @NotNull(message = ValidationMessage.PHONE_NUMBER_NOT_NULL)
  @ValidPhoneNumber
  private String phoneNumber;
  private List<CreateOrderDetailRequest> listProducts;
}
