package com.fu.skincare.response.bill;

import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BillByAccountResponse {
    private int id;
    private String createAt;
    private String address;
    private String phoneNumber;
    private int totalPrice;
    private String status;
    private List<OrderDetailResponse> listProducts;
}
