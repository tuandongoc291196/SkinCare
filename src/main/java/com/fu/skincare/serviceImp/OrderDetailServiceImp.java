package com.fu.skincare.serviceImp;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.fu.skincare.constants.Status;
import com.fu.skincare.constants.message.orderDetail.OrderDetailErrorMessage;
import com.fu.skincare.constants.message.product.ProductErrorMessage;
import com.fu.skincare.entity.OrderDetail;
import com.fu.skincare.entity.ProductDetail;
import com.fu.skincare.exception.ErrorException;
import com.fu.skincare.repository.OrderDetailRepository;
import com.fu.skincare.repository.ProductDetailRepostory;
import com.fu.skincare.request.orderDetail.CreateOrderDetailRequest;
import com.fu.skincare.response.orderDetail.OrderDetailResponse;
import com.fu.skincare.response.product.ProductDetailResponseByOrder;
import com.fu.skincare.service.OrderDetailService;
import com.fu.skincare.shared.Utils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImp implements OrderDetailService {

  private final OrderDetailRepository orderDetailRepository;
  private final ProductDetailRepostory productDetailRepository;
  private final ModelMapper modelMapper;

  @Override
  public OrderDetail createOrderDetail(CreateOrderDetailRequest request) {

    ProductDetail product = productDetailRepository.findById(request.getProductDetailId())
        .orElseThrow(() -> new ErrorException(ProductErrorMessage.NOT_FOUND));

    if (product.getQuantity() < request.getQuantity()) {
      throw new ErrorException(ProductErrorMessage.NOT_ENOUGH);
    }

    OrderDetail orderDetail = OrderDetail.builder()
        .productDetail(product)
        .quantity(request.getQuantity())
        .price(product.getPrice())
        .status(Status.ACTIVATED)
        .createAt(Utils.formatVNDatetimeNow())
        .build();

    return orderDetail;
  }

  @Override
  public OrderDetailResponse getOrderDetailById(int id) {

    OrderDetail orderDetail = orderDetailRepository.findById(id).orElseThrow(
        () -> new ErrorException(OrderDetailErrorMessage.ORDER_DETAIL_NOT_FOUND));

    OrderDetailResponse response = modelMapper.map(orderDetail, OrderDetailResponse.class);

    ProductDetailResponseByOrder productResponse = Utils.convertToProductDetailResponseByOrder(orderDetail.getProductDetail());

    response.setProductDetailResponse(productResponse);
    return response;
  }

}
