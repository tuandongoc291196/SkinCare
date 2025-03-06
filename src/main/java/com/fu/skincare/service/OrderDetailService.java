package com.fu.skincare.service;

import com.fu.skincare.entity.OrderDetail;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;

public interface OrderDetailService {
  public OrderDetailResponse getOrderDetailById(int id);

  public OrderDetail createOrderDetail(CreateOrderDetailRequest request);
}
